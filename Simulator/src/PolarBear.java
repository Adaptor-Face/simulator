
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple model of a polar bear. Bears age, move, eat seals, and die.
 *
 * @author Anders
 * @version 2017.02.24
 */
public class PolarBear extends Animal {
    // Characteristics shared by all bears (class variables).

    // The age at which a bear can start to breed.
    private static final int BREEDING_AGE = 356 * 4;
    // The age to which a bear can live.
    private static final int MAX_AGE = 356 * 30;
    // The likelihood of a bear breeding.
    private static final double BREEDING_PROBABILITY = 0.40;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single seal. In effect, this is the
    // number of steps a bear can go before it has to eat again.
    private static final int SEAL_FOOD_VALUE = 90;
    private static final int SEAL_FOOD_VALUE_PREG = 3 * SEAL_FOOD_VALUE;
    private static final int PREG_PERIOD = 27 * 9;
    private static final int MAX_FOOD_VALUE = 13 * SEAL_FOOD_VALUE;
    // A shared random number generator to control breeding.
    private static final Random RAND = Randomizer.getRandom();

    // Individual characteristics (instance fields).
    private int age;
    // The bear's food level, which is increased by eating seals.
    private int foodLevel;
    private int pregLevel;
    private int panicLevel;

    /**
     * Create a bear. A bear can be created as a new born (age zero and not
     * hungry) or with a random age and food level.
     *
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public PolarBear(boolean randomAge, Field field, Location location) {
        super(field, location);
        this.panicLevel = 0;
        if (randomAge) {
            age = RAND.nextInt(MAX_AGE);
            foodLevel = RAND.nextInt(MAX_FOOD_VALUE);
        } else {
            age = 0;
            foodLevel = 2 * SEAL_FOOD_VALUE;
            AnimalStatistics.addToStats(this.getClass(), "birth");
        }
    }

    /**
     * This is what the bear does most of the time: it hunts for seals. In the
     * process, it might breed, die of hunger, or die of old age.
     *
     * @param newBears A list to return newly born bears.
     * @return the location the animal moved to
     */
    @Override
    public Location act(List<Animal> newBears) {
        incrementAge();
        if (panicLevel > 0) {
            foodLevel -= panicLevel;
        } else {
        }
        foodLevel = incrementHunger(foodLevel);
        if (isAlive()) {
            giveBirth(newBears);
            // Move towards a source of food if found.
//            Location newLocation = findFood();
            Location newLocation;
            boolean canFindLand = getField().lookFor(getLocation(), Shore.class, "nswe", 12) != null;
            if (!canFindLand || panicLevel > 0) {
                panicLevel++;
                newLocation = panic();
                if (newLocation != null) {
                    newLocation = moveTo(newLocation);
                    if (getField().getLandscapeAt(newLocation).getType().equals(LandscapeType.SHALLOWS)) {
                        panicLevel = 0;
                    }
                }
            } else {
                newLocation = hunt();
            }
            if (newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Overcrowding.
                //setDead();
            }
        }
        return null;
    }

    /**
     * Increase the age. This could result in the bear's death.
     */
    private void incrementAge() {
        age++;
        if (age > MAX_AGE) {
            setDead("age");
        }
    }

    private boolean incrementPreg() {
        if (pregLevel == 1) {
            pregLevel--;
            return true;
        } else if (pregLevel == 0) {
            pregLevel = PREG_PERIOD;
        }
        pregLevel--;
        return false;
    }

    /**
     * Look for seals adjacent to the current location. Only the first live seal
     * is eaten.
     *
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            if (field.getLandscapeAt(where).getType().equals(LandscapeType.LAND)
                    || field.getLandscapeAt(where).getType().equals(LandscapeType.SHORE)
                    || field.getLandscapeAt(where).getType().equals(LandscapeType.SHALLOWS)
                    || field.getLandscapeAt(where).getType().equals(LandscapeType.OCEAN)) {
                Object animal = field.getAnimalAt(where);
                if (animal instanceof Seal) {
                    Seal seal = (Seal) animal;
                    if (seal.isAlive() && foodLevel < 10) {
                        seal.setDead("eaten");
                        AnimalStatistics.addToStats(seal.getClass(), AnimalStatistics.STAT_DEATH_EATEN);
                        if (pregLevel > 0) {
                            foodLevel = SEAL_FOOD_VALUE_PREG;
                        } else {
                            foodLevel = SEAL_FOOD_VALUE;
                        }
                        //System.out.println("A seal was brutally eaten alive");
                        return where;
                    }
                }
            }
        }
        return null;
    }

//    private Location hunt() {
//        Field field = getField();
//        List<Location> adjacent = field.adjacentLocationsRadius(getLocation(), 5);
//        Iterator<Location> it = adjacent.iterator();
//        while (it.hasNext()) {
//            Location where = it.next();
//            if (field.getLandscapeAt(where).getType().equals(LandscapeType.LAND)
//                    || field.getLandscapeAt(where).getType().equals(LandscapeType.SHORE)
//                    || field.getLandscapeAt(where).getType().equals(LandscapeType.SHALLOWS)
//                    || field.getLandscapeAt(where).getType().equals(LandscapeType.OCEAN)) {
//                Object animal = field.getAnimalAt(where);
//                             
//            }
//        }
//    }
    /**
     * Check whether or not this bear is to give birth at this step. New births
     * will be made into free adjacent locations.
     *
     * @param newBears A list to return newly born bears.
     */
    private void giveBirth(List<Animal> newBears) {
        // New bears are born into adjacent locations.
        // Get a list of adjacent free locations.
        if (incrementPreg()) {
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            for (int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                PolarBear young = new PolarBear(false, field, loc);
                newBears.add(young);
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
        if (canBreed() && RAND.nextDouble() <= BREEDING_PROBABILITY) {
            births = RAND.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A bear can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE;
    }

    @Override
    public Integer getFoodLevel() {
        return foodLevel;
    }

    @Override
    public List<String> getAnimalDetails() {
        List<String> info = new ArrayList<>();
        info.add("Species: PolarBear");
        info.add("Hunger: " + getFoodLevel());
        info.add("Age: " + age);
        info.add("Location: " + getLocation());
        return info;
    }

    @Override
    public int getAge() {
        return age;
    }

    private Location hunt() {
        Field field = getField();
        Location sealLocation = field.lookFor(getLocation(), Seal.class, "nswe");
        if (sealLocation == null) {
            return findFood();
        }
        if (ThreadLocalRandom.current().nextInt(0, MAX_FOOD_VALUE) < getFoodLevel()) {
            return getLocation();
        }
        Location moveLocation = moveTo(sealLocation);
        boolean fearless = true;
        if (field.getLandscapeAt(moveLocation).getType().equals(LandscapeType.SHALLOWS)) {
            //fearless = ThreadLocalRandom.current().nextDouble(0, 1) < 0.25;
        }
        if (fearless) {
            Animal animal = field.getAnimalAt(moveLocation);
            if (animal instanceof Seal) {
                Seal seal = (Seal) animal;
                if (seal.isAlive() && foodLevel < 10) {
                    seal.setDead("eaten");
                    AnimalStatistics.addToStats(seal.getClass(), AnimalStatistics.STAT_DEATH_EATEN);
                    if (pregLevel > 0) {
                        foodLevel = SEAL_FOOD_VALUE_PREG;
                    } else {
                        foodLevel = SEAL_FOOD_VALUE;
                    }
                    //System.out.println("A seal was brutally eaten alive");
                }
            }
        } else {
            Location loc = field.lookFor(getLocation(), Shore.class, "nesw");
            if (loc == null) {
                return findFood();
            }
            moveLocation = moveTo(loc);
        }
        return moveLocation;
    }

    private Location panic() {
        Location newLocation = getField().lookFor(getLocation(), Shore.class, "nswe", 16);

        return newLocation;
    }
}
