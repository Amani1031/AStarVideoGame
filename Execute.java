import processing.core.PImage;

import java.util.List;

public abstract class Execute extends Scheduleable{
    private int actionPeriod;

    public Execute(String id,
                   Point position,
                   List<PImage> images,
                   int imageIndex,
                   int animationPeriod,
                   int actionPeriod){
        super(id, position, images, imageIndex, animationPeriod);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() { return actionPeriod; }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
        super.scheduleActions(scheduler, world, imageStore);
    }

    abstract void executeEntityActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
}
