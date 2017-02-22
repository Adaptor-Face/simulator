
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field containing
 * rabbits and foxes.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Simulator {

    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;

    // List of animals in the field.
    private List<Animal> animals;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    private HashMap<Location, Class> sAnimals;
    private Field sField;
    private Field finishedField;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width, int... seed) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        sAnimals = new HashMap<>();
        field = new Field(depth, width, seed);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Seal.class, java.awt.Color.RED);
        view.setColor(PolarBear.class, java.awt.Color.BLUE);
        view.setColor(Land.class, java.awt.Color.GREEN);
        view.setColor(Shallows.class, java.awt.Color.RED);
        view.setColor(Shore.class, java.awt.Color.BLACK);
        view.setColor(Ocean.class, java.awt.Color.CYAN);

        // Setup a valid starting point.
        reset();
    }

    public Field getField() {
        return finishedField;
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation() {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     *
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }

    /**
     * Run the simulation from its current state for a single step. Iterate over
     * the whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep() {

        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();
        // Let all rabbits act.
        for (Iterator<Animal> it = animals.iterator(); it.hasNext();) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if (!animal.isAlive()) {
                it.remove();
            }
        }

        // Add the newly born foxes and rabbits to the main lists.
        animals.addAll(newAnimals);
        finishedField.resetField(field);

        view.showStatus(step, field);
        step++;
    }

    public int getStep() {
        return step;
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        animals.clear();
        populate();
        sField = new Field(DEFAULT_DEPTH, DEFAULT_WIDTH, field.getSeed());
        finishedField = new Field(DEFAULT_DEPTH, DEFAULT_WIDTH, field.getSeed());
        sField.resetField(field);
        finishedField.resetField(field);

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    public void softReset() {
        step = 0;
        animals.clear();
        if (sAnimals == null) {
            reset();
        } else {
            for (Location loc : sAnimals.keySet()) {
                if (sAnimals.get(loc).equals(PolarBear.class)) {
                    PolarBear polarBear = new PolarBear(true, field, loc);
                    animals.add(polarBear);
                } else {
                    Seal seal = new Seal(true, field, loc);
                    animals.add(seal);
                }
            }
        }
        if (sField != null) {
            field.resetField(sField);
        }

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
        sAnimals.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    PolarBear fox = new PolarBear(true, field, location);
                    animals.add(fox);
                    sAnimals.put(location, fox.getClass());
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Seal rabbit = new Seal(true, field, location);
                    animals.add(rabbit);
                    sAnimals.put(location, rabbit.getClass());
                }
                // else leave the location empty.
            }
        }
    }
}
