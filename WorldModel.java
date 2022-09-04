import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel
{
    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public int getNumRowsWorld() { return numRows; }

    public int getNumColsWorld() { return numCols; }

    public Set<Entity> getEntities() { return entities; }

    public void removeEntityAt(Point pos) {
        if (this.withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAt(entity.getPosition());
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            this.removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public void setBackgroundCell(
            Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    public Background getBackgroundCell(Point pos) {
        return background[pos.y][pos.x];
    }

    private void setOccupancyCell(
            Point pos, Entity entity)
    {
        occupancy[pos.y][pos.x] = entity;
    }

    public Entity getOccupancyCell(Point pos) {
        return occupancy[pos.y][pos.x];
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && this.getOccupancyCell(pos) != null;
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0
                && pos.x < this.numCols;
    }

    public Optional<Entity> findNearest(
            Point pos, List<Class> kinds)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Class kind: kinds)
        {
            for (Entity entity : entities) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return nearestEntity(ofType, pos);
    }

    /*
           Assumes that there is no entity currently occupying the
           intended destination cell.
        */
    public void addEntity(Entity entity) {
        if (this.withinBounds(entity.getPosition())) {
            this.setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }

    public void load(
            Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!Parser.processLine(in.nextLine(), this, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    public static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    private static int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.x - p2.x;
        int deltaY = p1.y - p2.y;

        return deltaX * deltaX + deltaY * deltaY;
    }




}
