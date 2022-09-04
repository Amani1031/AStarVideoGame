import processing.core.PImage;

import java.util.*;

public class Obstacle extends Scheduleable {

    public Obstacle(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex,
            int animationPeriod)
    {
        super(id, position, images, imageIndex, animationPeriod);
    }

}
