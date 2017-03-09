
import java.util.List;

/**
 * A class representing shared characteristics of animals.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public abstract class Animal {

    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's field.
    private Field field;
    // The animal's position in the field.
    private Location location;

    /**
     * Create a new animal at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location) {
        alive = true;
        this.field = field;
        setLocation(location);
    }

    /**
     * Make this animal act - that is: make it do whatever it wants/needs to do.
     *
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public Location act(List<Animal> newAnimals);

    /**
     * Returns the food level of the animal, null if it cant get hungry.
     *
     * @return the food level of the animal, null if it cant get hungry.
     */
    abstract public Integer getFoodLevel();

    /**
     * Returns an array with all the info about an animal.
     *
     * @return an array with all the info about an animal.
     */
    abstract public List<String> getAnimalDetails();

    /**
     * Check whether the animal is alive or not.
     *
     * @return true if the animal is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    abstract public int getAge();

    /**
     * Indicate that the animal is no longer alive. It is removed from the
     * field.
     */
    protected void setDead(String cause) {
        alive = false;
        AnimalStatistics.logSingleEvent(this.getClass(), cause, getAge(), getFoodLevel());
        AnimalStatistics.addToStats(this.getClass(), cause);
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     *
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the animal at the new location in the given field.
     *
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animal's field.
     *
     * @return The animal's field.
     */
    protected Field getField() {
        return field;
    }

    protected int incrementHunger(int foodLevel) {
        if (foodLevel <= 0) {
            setDead("starvation");
            return 0;
        }
        foodLevel--;
        return foodLevel;
    }

    protected Location moveTo(Location location) {
        int x = location.getCol() - getLocation().getCol();
        int y = location.getRow() - getLocation().getRow();
        int difference = Math.abs(x - y);
        int numX = 1;
        int numY = 1;
        if (x < 0) {
            numX = -1;
        }
        if (y < 0) {
            numY = -1;
        }
        Location moveLocation = null;

        if (x > y && (x / 2 + numX) >= difference) {
            moveLocation = new Location(getLocation().getRow(), getLocation().getCol() + numX);
        } else if (y > x && (y / 2 + numY) >= difference) {
            moveLocation = new Location(getLocation().getRow() + numY, getLocation().getCol());
        } else {
            moveLocation = new Location(getLocation().getRow() + numY, getLocation().getCol() + numX);
        }
        return moveLocation;
    }

}
