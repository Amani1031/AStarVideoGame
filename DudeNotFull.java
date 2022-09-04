import processing.core.PImage;

import java.util.*;

public class DudeNotFull extends Dude {


    public DudeNotFull(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod,
            int actionPeriod,
            int resourceLimit,
            int resourceCount) {
        super(id, position, images, imageIndex,
                animationPeriod, actionPeriod,
                resourceLimit, resourceCount);
    }

    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (getResourceCount() >= getResourceLimit()) {
            DudeFull miner = Factory.createDudeFull(getID(),
                    getPosition(), getActionPeriod(),
                    getAnimationPeriod(),
                    getResourceLimit(),
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    public void executeEntityActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> target =
                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (!target.isPresent() || !this.moveTo(world,
                target.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this,
                    Factory.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

}

