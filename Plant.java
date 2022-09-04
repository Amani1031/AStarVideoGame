import processing.core.PImage;

import java.util.List;

public abstract class Plant extends Execute{
    private int health;
    private int healthLimit;
    private static final String STUMP_KEY = "stump";

    public Plant(String id,
                 Point position,
                 List<PImage> images,
                 int imageIndex,
                 int animationPeriod,
                 int actionPeriod,
                 int health,
                 int healthLimit){
        super(id, position, images, imageIndex,
                animationPeriod, actionPeriod);
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public int getHealth() { return health; }
    public void setHealth(int add) { health += add; }
    public int getHealthLimit() { return healthLimit; }
    public String getStumpKey() { return STUMP_KEY; }

    abstract boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
}
