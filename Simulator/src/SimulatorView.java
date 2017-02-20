
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A graphical view of the simulation grid. The view displays a colored
 * rectangle for each location representing its contents. It uses a default
 * background color. Colors for each type of species can be defined using the
 * setColor method.
 *
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class SimulatorView extends JFrame {

    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;
    private Simulator sim;

    // A map for storing colors for participants in the simulation
    private Map<Class, Color> colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    /**
     * Create a view of the given width and height.
     *
     * @param height The simulation's height.
     * @param width The simulation's width.
     */
    public SimulatorView(int height, int width, Simulator sim) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        stats = new FieldStats();
        colors = new LinkedHashMap<>();
        this.sim = sim;

        setTitle("Fox and Rabbit Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        Container contents = getContentPane();
        JPanel topBar = new JPanel();
        JButton oneStepBtn = new JButton("One Step");
        oneStepBtn.setMnemonic(KeyEvent.VK_D);
        oneStepBtn.setActionCommand("ONE_STEP");
        oneStepBtn.addActionListener((ActionEvent e) -> {
            sim.simulateOneStep();
        });
        JTextField stepTextField = new JTextField("AutoStep");
        JButton multiStep = new JButton("AutoStep");
        multiStep.setMnemonic(KeyEvent.VK_D);
        multiStep.setActionCommand("Auto");
        multiStep.addActionListener((ActionEvent e) -> {
            System.out.println("TOSK");
        });
        topBar.add(oneStepBtn, BorderLayout.WEST);
        topBar.add(stepLabel, BorderLayout.NORTH);
        topBar.add(stepTextField, BorderLayout.EAST);
        topBar.add(multiStep, BorderLayout.EAST);
        contents.add(topBar, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(population, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    /**
     * Define a color to be used for a given class of animal.
     *
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class animalClass, Color color) {
        colors.put(animalClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class objClass) {
        Color col = colors.get(objClass);
        if (col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     *
     * @param step Which iteration step it is.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Field field) {
        if (!isVisible()) {
            setVisible(true);
        }

        stepLabel.setText(STEP_PREFIX + step);
        stats.reset();

        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getAnimalAt(row, col);
                Landscape landscape = field.getLandscapeAt(row, col);
                if (animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                } else {
                    fieldView.drawMark(col, row, getColor(landscape.getClass()));
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determine whether the simulation should continue to run.
     *
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }
}

  