import processing.core.PImage;

import java.util.List;

public class Factory {

    private static final int SAPLING_HEALTH_LIMIT = 5;
    private static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time

    public static Action createAnimationAction(Scheduleable entity, int repeatCount) {
        return new Animation(entity, null, null,
                repeatCount);
    }

    public static Action createActivityAction(
            Execute entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }

    public static House createHouse(
            String id, Point position, List<PImage> images)
    {
        return new House(id, position, images, 0);
    }

    public static Obstacle createObstacle(
            String id, Point position, int animationPeriod, List<PImage> images)
    {
        return new Obstacle(id, position, images, 0, 0);
    }

    public static Tree createTree(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new Tree(id, position, images, 0, animationPeriod,
                actionPeriod, health, 0);
    }

    public static Stump createStump(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Stump(id, position, images, 0);
    }

    // health starts at 0 and builds up until ready to convert to Tree
    public static Sapling createSapling(
            String id,
            Point position,
            List<PImage> images)
    {
        return new Sapling(id, position, images, 0,
                SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD,
                0, SAPLING_HEALTH_LIMIT);
    }

    public static Fairy createFairy(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Fairy(id, position, images, 0, animationPeriod, actionPeriod);
    }

    // need resource count, though it always starts at 0
    public static DudeNotFull createDudeNotFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images)
    {
        return new DudeNotFull(id, position, images, 0,
                animationPeriod, actionPeriod, resourceLimit, 0);
    }

    // don't technically need resource count ... full
    public static DudeFull createDudeFull(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new DudeFull(id, position, images, 0,
                animationPeriod, actionPeriod, resourceLimit, 0);
    }
}
