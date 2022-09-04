import processing.core.PImage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Fairy extends Execute {
    private static final String SAPLING_KEY = "sapling";


    public Fairy(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod)
    {
        super(id, position, images, imageIndex, animationPeriod, actionPeriod);
    }


    public void executeEntityActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveToFairy(world, fairyTarget.get(), scheduler)) {
                Sapling sapling = Factory.createSapling("sapling_" + getID(), tgtPos,
                        imageStore.getImageList(SAPLING_KEY));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }


    private Point nextPositionFairy(WorldModel world, Point destPos)
    {
        PathingStrategy strategy = new SingleStepPathingStrategy();
        //PathingStrategy strategy = new AStarPathingStrategy();

        List<Point> path = strategy.computePath(getPosition(), destPos,
                p -> (!world.isOccupied(p) && world.withinBounds(p)),
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



    private boolean moveToFairy(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = this.nextPositionFairy(world, target.getPosition());

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

    private boolean adjacent(Point p1, Point p2) {
        return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) || (p1.y == p2.y
                && Math.abs(p1.x - p2.x) == 1);
    }

}
