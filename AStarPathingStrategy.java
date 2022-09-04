import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy
{
    private class PathNode{
        private Point location;
        private double g;
        private double h;
        private double f;
        private Point prior;

        public PathNode(Point location, double g, double h, double f, Point prior){
            this.location = location;
            this.g = g;
            this.h = h;
            this.f = f;
            this.prior = prior;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathNode pathNode = (PathNode) o;
            return Objects.equals(location, pathNode.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location);
        }


        public double getF() { return f; }
    }

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        PathNode startingNode = new PathNode(start, 0, distanceSquared(start, end),
                distanceSquared(start, end), null); //creates a starting Node to put in the priority queue later

        PriorityQueue<PathNode> openList = new PriorityQueue<>(Comparator.comparing(PathNode::getF)); //intialises openList as priority queue
        Map<Point, PathNode> closedList = new HashMap<>(); //intialises closedList as map

        List<Point> finalPath = aStarAlgorithm(startingNode, start, end, //calls the A-star algorithm
                openList, closedList,
                canPassThrough,withinReach,
                potentialNeighbors);
        if (finalPath == null){ //if final path from A-Star method is empty, it outputs an empty list
            List<Point> route = new LinkedList<>();
            return route;
        }
        return finalPath;
    }

    private List<Point> aStarAlgorithm(PathNode startingNode, Point start, Point end,
                                       PriorityQueue<PathNode> openList,
                                       Map<Point, PathNode> closedList,
                                       Predicate<Point> canPassThrough,
                                       BiPredicate<Point, Point> withinReach,
                                       Function<Point, Stream<Point>> potentialNeighbors){
        openList.add(startingNode);
        while (!openList.isEmpty()){
            PathNode current = openList.poll(); //Retrieves and removes the head of openList
            closedList.put(current.location, current); //Adds current PathNode to the closed list
            if (withinReach.test(current.location, end)){
                Point value = current.location;
                return createPath(closedList, value); //Returns the path created
            }
            else {
                List<Point> neighborPoints = potentialNeighbors.apply(current.location) //Generate list of points of neighbors
                        .filter(canPassThrough)
                        .collect(Collectors.toList());
                List<PathNode> neighbors = neighborPathNodeFactory(start,current,
                        neighborPoints,
                        end); // turns the list of points of neighbors into nodes
                for (PathNode neighbor : neighbors){
                    if (!closedList.containsKey(neighbor.location)){
                        openList = openListProcessing(openList, neighbor); //Deletes identical nodes for which the f-value is larger
                                                                                            //Adds the neighbor being processed into the openList
                    }
                }
            }
        }
        return null;
    }


    private LinkedList<Point> createPath(Map<Point, PathNode> closedList, Point value){
        LinkedList<Point> path = new LinkedList<>();
        path.add(value);
        while (value != null){
            PathNode now = closedList.get(value);
            path.addFirst(now.prior);
            value = now.prior;
        }
        path.remove();
        path.remove();
        return path;
    }

    private PriorityQueue<PathNode> openListProcessing(PriorityQueue<PathNode> openList, PathNode neighbor){

        //If there is an identical node already in openList, the one with the lower f-value stays
        //If there isn't already an identical node, add the new node in

        for (PathNode node : openList){
            if (node.equals(neighbor)){
                if (node.g > neighbor.g){
                    openList.remove(node);
                    openList.add(neighbor);

                }
                return openList;
            }
        }
        openList.add(neighbor);
        return openList;
    }

    private List<PathNode> neighborPathNodeFactory(Point start, PathNode current, List<Point> neighbors, Point end){

        //Turns every neighbor into a pathnode of its own and returns the list of neighbors as nodes

        //DO I HAVE TO BE STORING H AND G
        List<PathNode> neighborNodes = new ArrayList<>();
        for (Point neighbor : neighbors){
            double g = current.g + 1;
            double h = distanceSquared(neighbor, end);
            double f = g + h;
            PathNode node = new PathNode(neighbor, g, h, f, current.location);
            neighborNodes.add(0, node);
        }
        return neighborNodes;
    }

    private double distanceSquared(Point p1, Point p2) {
        double deltaX = p1.x - p2.x;
        double deltaY = p1.y - p2.y;
        deltaX = Math.pow(deltaX, 2);
        deltaY = Math.pow(deltaY, 2);
        return Math.pow(deltaX+deltaY, 0.5);
    }
}
