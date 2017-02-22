
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represent a rectangular grid of field positions. Each position is able to
 * store a single animal.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Field {

    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();

    // The depth and width of the field.
    private int depth, width;
    // Storage for the animals.
    private HashMap<Location, Animal> field;
    private HashMap<Location, Landscape> landscape;
    private int seed;

    /**
     * Represent a field of the given dimensions.
     *
     * @param depth The depth of the field.
     * @param width The width of the field.
     * @param seed The seed to change the landscape (optional).
     */
    public Field(int depth, int width, int... seed) {
        this.depth = depth;
        this.width = width;
        field = new HashMap<>();
        landscape = new HashMap<>(createLandscape(depth, width, seed));
    }

    private HashMap<Location, Animal> getField() {
        return field;
    }

    private HashMap<Location, Landscape> getLandscape() {
        return landscape;
    }

    public int getSeed() {
        return seed;
    }

    public void resetField(Field field) {
        this.field.clear();
        this.field.putAll(field.getField());
    }

    private HashMap<Location, Landscape> createLandscape(int depth, int width, int... seed) {
        int landscapeSeed = 812;
        if (seed.length == 1) {
            landscapeSeed = seed[0];
            
        }
        this.seed = landscapeSeed;
        boolean noVariation = landscapeSeed == 0;
        HashMap<Location, Landscape> field = new HashMap<>();
        boolean inversed = landscapeSeed < 0;
        int middle = Integer.parseInt("" + width / 2);
        for (int y = 0; y <= depth; y++) {
            for (int x = 0; x <= width; x++) {
                field.put(new Location(y, x), createLand(x, middle, inversed));
            }
            if (!noVariation) {
                int number = middle;
                number = Math.abs(((landscapeSeed ^ middle + middle ^ landscapeSeed) - ((middle + landscapeSeed) * (y ^ 2 - y))) % 10) - 5;

                middle += number;
                if (middle < 0) {
                    middle = 0;
                } else if (middle > 120) {
                    middle = 120;
                }
            }
        }
        return field;
    }

    /**
     * Empty the field.
     */
    public void clear() {
        field.clear();
    }

    /**
     * Clear the given location.
     *
     * @param location The location to clear.
     * @return true if the location contained animals.
     */
    public boolean clear(Location location) {
        if (field.containsKey(location)) {
            field.remove(location);
            return true;
        }
        return false;
    }

    /**
     * Place an animal at the given location. If there is already an animal at
     * the location it will be lost.
     *
     * @param animal The animal to be placed.
     * @param row Row coordinate of the location.
     * @param col Column coordinate of the location.
     */
    public void place(Animal animal, int row, int col) {
        place(animal, new Location(row, col));
    }

    /**
     * Place an animal at the given location. If there is already an animal at
     * the location it will be lost.
     *
     * @param animal The animal to be placed.
     * @param location Where to place the animal.
     */
    public void place(Animal animal, Location location) {
        field.put(location, animal);
    }

    /**
     * Return the animal at the given location, if any.
     *
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Animal getAnimalAt(Location location) {
        return field.get(location);
    }

    /**
     * Return the landscape at the given location, if any.
     *
     * @param location Where in the field.
     * @return The landscape at the given location, or null if there is none.
     */
    public Landscape getLandscapeAt(Location location) {
        return landscape.get(location);
    }

    /**
     * Return the animal at the given location, if any.
     *
     * @param row The desired row.
     * @param col The desired column.
     * @return The animal at the given location, or null if there is none.
     */
    public Animal getAnimalAt(int row, int col) {
        return field.get(new Location(row, col));
    }

    /**
     * Return the landscape at the given location.
     *
     * @param row The desired row.
     * @param col The desired column.
     * @return The landscape at the given location, or null if there is none.
     */
    public Landscape getLandscapeAt(int row, int col) {
        return landscape.get(new Location(row, col));
    }

    /**
     * Return the animal at the given location, if any.
     *
     * @param row The desired row.
     * @param col The desired column.
     * @return The animal at the given location, or null if there is none.
     */
    public Object getObjectAt(int row, int col) {
        Object obj = field.get(new Location(row, col));
        if (obj == null) {
            return landscape.get(new Location(row, col));
        }
        return obj;
    }

    /**
     * Return the landscape at the given location.
     *
     * @param row The desired row.
     * @param col The desired column.
     * @return The landscape at the given location, or null if there is none.
     */
    public Object getObjectAt(Location location) {
        Object obj = field.get(location);
        if (obj == null) {
            return landscape.get(location);
        }
        return obj;
    }

    /**
     * Generate a random location that is adjacent to the given location, or is
     * the same location. The returned location will be within the valid bounds
     * of the field.
     *
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location randomAdjacentLocation(Location location) {
        List<Location> adjacent = adjacentLocations(location);
        return adjacent.get(0);
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     *
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location) {
        List<Location> free = new LinkedList<Location>();
        List<Location> adjacent = adjacentLocations(location);
        for (Location next : adjacent) {
            if (Field.this.getAnimalAt(next) == null) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Try to find a free location that is adjacent to the given location. If
     * there is none, return null. The returned location will be within the
     * valid bounds of the field.
     *
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location freeAdjacentLocation(Location location) {
        // The available free ones.
        List<Location> free = getFreeAdjacentLocations(location);
        if (free.size() > 0) {
            return free.get(0);
        } else {
            return null;
        }
    }

    /**
     * Return a shuffled list of locations adjacent to the given one. The list
     * will not include the location itself. All locations will lie within the
     * grid.
     *
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> adjacentLocations(Location location) {
        assert location != null : "Null location passed to adjacentLocations";
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<Location>();
        if (location != null) {
            int row = location.getRow();
            int col = location.getCol();
            for (int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if (nextRow >= 0 && nextRow < depth) {
                    for (int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }

            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Return the depth of the field.
     *
     * @return The depth of the field.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Return the width of the field.
     *
     * @return The width of the field.
     */
    public int getWidth() {
        return width;
    }

    private Landscape createLand(int x, int middle, boolean inversed) {
        Landscape landType = null;
        if (inversed) {
            if (x - 8 >= middle) {
                landType = new Land();
            } else if (x >= middle) {
                landType = new Shore();
            } else if (x + 8 > middle) {
                landType = new Shallows();
            } else {
                landType = new Ocean();
            }
        } else if (x + 8 <= middle) {
            landType = new Ocean();
        } else if (x <= middle) {
            landType = new Shallows();
        } else if (x - 8 > middle) {
            landType = new Land();
        } else {
            landType = new Shore();
        }
        return landType;
    }
}
