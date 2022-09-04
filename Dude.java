import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class Dude extends Execute {
    private int resourceLimit;
    private int resourceCount;


    public Dude(String id,
                Point position,
                List<PImage> images,
                int imageIndex,
                int animationPeriod,
                int actionPeriod,
                int resourceLimit,
                int resourceCount){
        super(id, position, images, imageIndex, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public int getResourceLimit() { return resourceLimit; }

    public int getResourceCount() { return resourceCount; }

    public boolean moveTo(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (adjacent(this.getPosition(), target.getPosition())) {
            if (target instanceof Plant){
                resourceCount++;
                ((Plant)target).setHealth(-1);
            }
            return true;
        }
        else {
            Point nextPos = this.nextPositionDude(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private Point nextPositionDude(
            WorldModel world, Point destPos)
    {

        PathingStrategy strategy = new SingleStepPathingStrategy();
        //PathingStrategy strategy = new AStarPathingStrategy();

        List<Point> path = strategy.computePath(getPosition(), destPos,
                p -> (world.withinBounds(p)) && (!world.isOccupied(p) || world.getOccupancyCell(p).getClass().equals(Stump.class)),
                (p1, p2) -> neighbors(p1,p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
                                                //DIAGONAL_NEIGHBORS);
                                                //DIAGONAL_CARDINAL_NEIGHBORS);

        if (path.size() == 0){
            return getPosition();
        }
        return path.get(0);
    }

    private static boolean neighbors(Point p1, Point p2)
    {
        return p1.x+1 == p2.x && p1.y == p2.y ||
                p1.x-1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y+1 == p2.y ||
                p1.x == p2.x && p1.y-1 == p2.y;
    }

    private static final Function<Point, Stream<Point>> DIAGONAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.x - 1, point.y - 1))
                            .add(new Point(point.x + 1, point.y + 1))
                            .add(new Point(point.x - 1, point.y + 1))
                            .add(new Point(point.x + 1, point.y - 1))
                            .build();



    private static final Function<Point, Stream<Point>> DIAGONAL_CARDINAL_NEIGHBORS =
            point ->
                    Stream.<Point>builder()
                            .add(new Point(point.x - 1, point.y - 1))
                            .add(new Point(point.x + 1, point.y + 1))
                            .add(new Point(point.x - 1, point.y + 1))
                            .add(new Point(point.x + 1, point.y - 1))
                            .add(new Point(point.x, point.y - 1))
                            .add(new Point(point.x, point.y + 1))
                            .add(new Point(point.x - 1, point.y))
                            .add(new Point(point.x + 1, point.y))
                            .build();

    private boolean adjacent(Point p1, Point p2) {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y
                && Math.abs(p1.x - p2.x) == 1);
    }


}
