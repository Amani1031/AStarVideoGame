import processing.core.PImage;

import java.util.*;

public class Sapling extends Plant {

    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;
    private static final String TREE_KEY = "tree";

    public Sapling(String id,
                   Point position,
                   List<PImage> images,
                   int imageIndex,
                   int animationPeriod,
                   int actionPeriod,
                   int health,
                   int healthLimit)
    {
        super(id, position, images, imageIndex,
                animationPeriod, actionPeriod, health, healthLimit);
    }

    public boolean transform(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (getHealth() <= 0) {
            Stump stump = Factory.createStump(getID(),
                    getPosition(),
                    imageStore.getImageList(getStumpKey()));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            //((Scheduleable)stump).scheduleActions(scheduler, world, imageStore);

            return true;
        }
        else if (getHealth() >= getHealthLimit())
        {
            Tree tree = Factory.createTree("tree_" + getID(),
                    getPosition(),
                    getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN),
                    getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
                    getNumFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN),
                    imageStore.getImageList(TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    public void executeEntityActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        setHealth(1);
        if (!this.transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

    private int getNumFromRange(int max, int min)
    {
        Random rand = new Random();
        return min + rand.nextInt(
                max
                        - min);
    }


}
