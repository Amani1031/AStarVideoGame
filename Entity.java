import processing.core.PImage;

import java.util.List;

public abstract class Entity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images, int imageIndex){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public String getID() { return id; }
    public Point getPosition() { return position; }
    public void setPosition(Point point) { position = point; }
    public List<PImage> getImages() { return images; }
    public int getImageIndex() { return imageIndex; }
    public void setImageIndex(int index) { imageIndex = index; }

    public PImage getCurrentImage() {
        return (this).images.get(this.imageIndex);
    }

    public void tryAddEntity(WorldModel world) {
        if (world.isOccupied(position)) {
            throw new IllegalArgumentException("position occupied");
        }
        world.addEntity(this);
    }

}



