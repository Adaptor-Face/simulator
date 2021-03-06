
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple model of a rabbit. Rabbits age, move, breed, and die.
 *
 * @author Erik
 * @version 2011.07.31
 */
public class Seal extends Animal {
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 1602;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 9790;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    private static final int FISH_FOOD_VALUE = 2;

    private static final int LAND_FOOD_VALUE = 1;
    private static final double FISH_CONSTANT = 0.85;
    private static final int PREG_PERIOD = 1 * 27;
    private static final int STARVATION_PERIOD = 5;
    private static final int FOOD_LIMIT = 28;

    // Individual characteristics (instance fields).
    private int foodLevel;
    private int pregLevel;
    private boolean starved = false;
    private Location lastLocation;
    private boolean huntMode = true;

    private Landscape ls = new Landscape();

    // The rabbit's age.
    private int age;

    /**
     * Create a new rabbit. A rabbit may be created with age zero (a new born)
     * or with a random age.
     *
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Seal(boolean randomAge, Field field, Location location) {
        super(field, location);
        double randomSelection = ThreadLocalRandom.current().nextDouble(0, 1);
        if (randomSelection < 0.21) {
            pregLevel = rand.nextInt(26);
        }
        age = 0;
        foodLevel = FOOD_LIMIT;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(20);
        } else {
            AnimalStatistics.addToStats(this.getClass(), "birth");
        }
    }

    /**
     * This is what the rabbit does most of the time - it runs around. Sometimes
     * it will breed or die of old age.
     *
     * @param newSeals A list to return newly born rabbits.
     */
    public Location act(List<Animal> newSeals) {
        Field field = getField();
        if (foodLevel > 17) {
            huntMode = false;
        }
        //incrementFood();
        if (foodLevel >= FOOD_LIMIT) {
            foodLevel = FOOD_LIMIT - 1;
        }
        incrementAge();
        foodLevel = incrementHunger(foodLevel);
        if (isAlive()) {
            giveBirth(newSeals);
            ls = getField().getLandscapeAt(getLocation());
            Location newLocation;
            Location currentLocation = getLocation();
            Location oceanTile = scanForOceanTile(getLocation());
            if (oceanTile != null && (lastLocation != getLocation())) {
                newLocation = oceanTile;
            } else {
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // Try to move into a free location.
            if (!huntMode) {
                newLocation = chillinOnTheBeach();
                if (foodLevel <= 10) {
                    huntMode = true;
                }
            } else if (newLocation != null) {
                lastLocation = getLocation();
                if (((newLocation == oceanTile) && (oceanTile != null))) {
                    findFood(oceanTile);
                }
            } else {
                // Overcrowding.
                //setDead();
            }
            if (newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            setLocation(newLocation);
        }
        return null;
    }

    @Override
    public Integer getFoodLevel() {
        return foodLevel;
    }

    /**
     * Increase the age. This could result in the rabbit's death.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead("age");
            //System.out.println("DEATH ACTIVATED");
        }
    }

    /**
     * Check whether or not this rabbit is to give birth at this step. New
     * births will be made into free adjacent locations.
     *
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Animal> newSeals) {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        if (incrementPreg()) {
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            for (int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Seal young = new Seal(false, field, loc);
                newSeals.add(young);
            }
        }
    }

    /**
     * Generate a number representing the number of births, if it can breed.
     *
     * @return The number of births (may be zero).
     */
    private int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     *
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    private void findFood(Location location) {
        double min = 0;
        double max = 1;
        Field field = getField();
        Landscape tt = field.getLandscapeAt(location);
        double randomFishValue = ThreadLocalRandom.current().nextDouble(min, max);
        if (tt != null) {
            if (tt.getType().equals(LandscapeType.OCEAN) || tt.getType().equals(LandscapeType.SHALLOWS)) {
                if (randomFishValue <= tt.getFoodDensitiy()) {
                    foodLevel += FISH_FOOD_VALUE;
                }
            }
            if (tt.getType().equals(LandscapeType.SHORE)) {
                if (randomFishValue <= tt.getFoodDensitiy()) {
                    foodLevel += LAND_FOOD_VALUE;
                }
            }
        } else {
            System.out.println("ls = null " + location);
        }
    }

    private Location scanForOceanTile(Location location) {
        Field field = getField();
        List<Location> adjacent = field.getFreeAdjacentLocations(location);
        Landscape tt = null;
        for (Location cT : adjacent) {
            tt = field.getLandscapeAt(cT);
            if ((tt.getType().equals(LandscapeType.OCEAN)) || (tt.getType().equals(LandscapeType.SHALLOWS) || (tt.getType().equals(LandscapeType.SHORE)))) {
                return cT;
            }
        }
        return null;
    }

    private boolean incrementPreg() {
        pregLevel--;
        if (pregLevel == 0) {
            pregLevel = PREG_PERIOD;
            return true;
        }
        return false;
    }

    private boolean setStarved() {
        if (foodLevel == 0) {
            starved = true;
        }
        return starved;
    }

    private void incrementFood() {
        setStarved();
        foodLevel = incrementHunger(foodLevel);
        if (foodLevel == 1 && !starved) {
            foodLevel = STARVATION_PERIOD;
        } else if (foodLevel == 1) {
            foodLevel = incrementHunger(foodLevel);
        }
    }

    @Override
    public List<String> getAnimalDetails() {
        List<String> info = new ArrayList<>();
        info.add("Species: Seal");
        info.add("Hunger: " + getFoodLevel());
        info.add("Age: " + age);
        return info;
    }

    private boolean findSeal(List<Location> locations) {
        Field field = getField();
        for (Location where : locations) {
            if (field.getAnimalAt(where) instanceof Seal) {
                return true;
            }
        }
        return false;
    }

    private Location chillinOnTheBeach() {
        Field field = getField();
        Location where = getLocation();
        Location location = null;
        location = field.lookFor(where, Shore.class, "N S E W");
        if (location != null) {
            location = moveTo(location);
        } else {
            location = itsDangerousToGoAlone();
        }
        return location;
    }

    private Location itsDangerousToGoAlone() {
        Field field = getField();
        Location where = getLocation();
        Location location = field.lookFor(where, Seal.class, "N S E W");
        int xCord = where.getCol();
        int yCord = where.getRow();
        List<Location> locs = field.getFreeAdjacentLocations(location);
        for (Location lc : locs) {
            if (((xCord == lc.getCol()) || (yCord == lc.getRow())) & !(xCord == lc.getCol() && yCord == lc.getRow())) {
                location = moveTowards(lc, where);
                return location;
            } else {
                List<Location> randomLocs = field.getFreeAdjacentLocations(where);
                for (Location lcc : randomLocs) {
                    location = lcc;
                    return location;
                }
            }
        }
        return null;
    }

    private Location moveTowards(Location location, Location where) {
        Field field = getField();
        where = where;
        location = location;
        Location newLocation = null;
        int distance = where.distanceBetween(location);
        int i = 1;
        if (location.getRow() > location.getCol()) {
            newLocation = (new Location(location.getRow() - distance + i, location.getCol()));
        } else if (location.getRow() < location.getCol()) {
            newLocation = (new Location(location.getRow(), location.getCol() - distance + i));
        } else if (field.getLandscapeAt(getLocation()).getType().equals(LandscapeType.LAND)) {
            newLocation = (new Location(location.getRow(), location.getCol() - distance - i));
        } else {
            newLocation = (new Location(location.getRow(), location.getCol() - distance + i));
        }

        /*
        if (where.getCol() == location.getCol()) {
            newLocation = (new Location(location.getRow() - distance + i, location.getCol()));
        } else if (where.getRow() == location.getRow()) {
            newLocation = (new Location(location.getRow(), location.getCol() - distance + i));
        } */
        return newLocation;
    }

    @Override
    public int getAge() {
        return age;
    }
}
