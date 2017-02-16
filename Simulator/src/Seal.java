import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author Erik
 * @version 2011.07.31
 */
public class Seal extends Animal {
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 2860;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 1456;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.12;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    private static final int FISH_FOOD_VALUE = 1;
    private static final double FISH_CONSTANT = 0.85;
    
    // Individual characteristics (instance fields).
    private int foodLevel;
    
    private Landscape ls = new Landscape();
    
    // The rabbit's age.
    private int age;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Seal(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        foodLevel = rand.nextInt(20);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(20);
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newSeals A list to return newly born rabbits.
     */
    public Location act(List<Animal> newSeals)
    {
        incrementAge();
        foodLevel = incrementHunger(foodLevel);
        if(isAlive()) {
            ls = getField().getLandscapeAt(getLocation());
            giveBirth(newSeals);
            Location newLocation;
            Location oceanTile = scanForOceanTile(getLocation());
            if (oceanTile != null) {
                findFood(oceanTile);
                newLocation = oceanTile;
                System.out.println(ls.getType());
            } else {
                newLocation = getField().freeAdjacentLocation(getLocation());
                System.out.println("I AM HERE: " + getField().getLandscapeAt(newLocation).getType() + "AND IS THIS HUNGRY: " + getFoodLevel());
            }
            // Try to move into a free location.
            if(newLocation != null) {
                setLocation(newLocation);
                return newLocation;
            }
            else {
                // Overcrowding.
                setDead();
                System.out.println("Crowded");
            }
        } 
        return null;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            System.out.println("Died of old age");
            setDead();
            System.out.println("Died of old age");
        }
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Animal> newSeals)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Seal young = new Seal(false, field, loc);
            newSeals.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     * @return true if the rabbit can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
        private void findFood(Location location)
    {
        double min = 0;
        double max = 1;
        double randomFishValue = ThreadLocalRandom.current().nextDouble(min, max);
            if(randomFishValue <= ls.getFoodDensitiy()) {
                    foodLevel += FISH_FOOD_VALUE;
                    System.out.println(foodLevel);
                    System.out.println(location.getCol() + ", " +  location.getRow());
            }
    }
        
        private Location scanForOceanTile(Location location) {
            Field field = getField();
            List<Location> adjacent = field.getFreeAdjacentLocations(location);
            for (Location cT : adjacent) {
                if ((cT.equals(LandscapeType.OCEAN)) || (cT.equals(LandscapeType.SHALLOWS))) {
                    return cT;
                }            
        }
            return null;
        }
}
