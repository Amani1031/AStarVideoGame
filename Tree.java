import processing.core.PImage;

import java.util.*;

public class Tree extends Plant {


    public Tree(String id,
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
            //((Entity)stump).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void executeEntityActivity(
        WorldModel world,
        ImageStore imageStore,
        EventScheduler scheduler)
    {

        if (!this.transform(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

}

