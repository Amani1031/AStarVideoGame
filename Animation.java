/**
 * An action that can be taken by an entity
 */
public final class Animation implements Action
{
    private Scheduleable entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Animation(
            Scheduleable entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }


    public void executeAction(EventScheduler scheduler) {
        entity.nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent((Entity)entity,
                    Factory.createAnimationAction(entity,
                            Math.max(repeatCount - 1,
                                    0)),
                    entity.getAnimationPeriod());
        }
    }
}