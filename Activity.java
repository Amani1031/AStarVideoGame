/**
 * An action that can be taken by an entity
 */
public final class Activity implements Action
{
    private Execute entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(Execute entity, WorldModel world, ImageStore imageStore, int repeatCount){
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        entity.executeEntityActivity(world, imageStore, scheduler);

    }
}