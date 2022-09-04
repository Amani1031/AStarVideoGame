import processing.core.PImage;

import java.util.*;

public class DudeFull extends Dude {

    public DudeFull(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod,
            int resourceLimit,
            int resourceCount)
    {
        super(id, position, images, imageIndex,
                animationPeriod, actionPeriod,
                resourceLimit, resourceCount);
    }


    public void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        DudeNotFull miner = Factory.createDudeNotFull(getID(),
                getPosition(), getActionPeriod(),
                getAnimationPeriod(),
                getResourceLimit(),
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    public void executeEntityActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world,
                fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

}
