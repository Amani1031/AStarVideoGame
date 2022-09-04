import processing.core.PImage;

import java.util.*;

public class House extends Entity {

    public House(
            String id,
            Point position,
            List<PImage> images,
            int imageIndex)
    {
        super(id, position, images, imageIndex);
    }

}
