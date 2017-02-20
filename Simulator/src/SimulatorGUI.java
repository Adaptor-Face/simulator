
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Kristoffer
 */
public class SimulatorGUI extends Application {

    private BorderPane root;
    private Simulator sim;
    private int depth = 80;
    private int width = 120;
    private FieldView fieldView;
    private Map<Class, Color> colors = new LinkedHashMap<>();
    private Map<Class, java.awt.Color> colorsAwt = new LinkedHashMap<>();
    private ArrayList<Rectangle> gridNodes = new ArrayList<>();
    private static final Color UNKNOWN_COLOR = Color.GREY;
    private static final java.awt.Color UNKNOWN_AWT_COLOR = java.awt.Color.GRAY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.sim = new Simulator();
        this.root = createScene(primaryStage);
        GridPane gp = new GridPane();
        //createSimulatorWindow(primaryStage);
        gp.add(root, 0, 0);
        BorderPane test = new BorderPane();
        test.setCenter(root);
        Scene scene = new Scene(test, 450, 100);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulator!");
        primaryStage.show();
    }

    private BorderPane createScene(Stage primaryStage) {

        BorderPane root = new BorderPane();
        HBox toolBar = new HBox();
        Button back = new Button("One Step");
        back.setOnAction((ActionEvent event) -> {
            simulateOneStep();
        });
        TextField stepInput = new TextField();
        stepInput.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            try {
                if (newValue.length() > 0 && newValue.length() <= 8) {
                    Integer.parseInt(newValue);
                } else if (newValue.length() == 0) {
                    stepInput.setText("");
                } else {
                    stepInput.setText(oldValue);
                }
            } catch (NumberFormatException e) {
                stepInput.setText(oldValue);
            }
        });
        Button multiStep = new Button("Step");
        multiStep.setOnAction((ActionEvent event) -> {
            simulate(Integer.parseInt(stepInput.getText()));
        });
        Button reset = new Button("Reset");
        reset.setOnAction((ActionEvent event) -> {
            sim.reset();
        });
        toolBar.getChildren().add(back);
        toolBar.getChildren().add(stepInput);
        toolBar.getChildren().add(multiStep);
        toolBar.getChildren().add(reset);
        root.setTop(toolBar);
        Scene scene = new Scene(root, 450, 350);
        primaryStage.setScene(scene);
        return root;
    }

    private void createSimulatorWindow(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(0.8);
        gridPane.setHgap(0.8);
        gridPane.setAlignment(Pos.CENTER);
        root.setCenter(gridPane);
        setColorsForClasses();
        gridPane.setMaxHeight(Double.MAX_VALUE);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        createGrid(gridPane);
    }

    private void createGrid(GridPane gridPane) {
        for (int y = 0; y < depth; y++) {
            for (int x = 0; x < width; x++) {
                Rectangle square = new Rectangle(5, 5, UNKNOWN_COLOR);
                square.setId(new Location(y, x).toString());
                square.setOnMouseClicked((MouseEvent event) -> {
                    System.out.println(square.getId());
                });
                gridPane.add(square, x, y);
                gridNodes.add(square);
            }
        }
        showStatus();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showStatus() {
        gridNodes.forEach((Rectangle k) -> k.setFill(getColor(sim.getField().getObjectAt(new Location(k.getId())).getClass())));
    }

    /**
     * Define a color to be used for a given class of animal.
     *
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public final void setColor(Class animalClass, Color color) {
        colors.put(animalClass, color);
    }

    /**
     * Define a color to be used for a given class of animal.
     *
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public final void setAwtColor(Class animalClass, java.awt.Color color) {
        colorsAwt.put(animalClass, color);
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
     * @return The color to be used for a given class of animal.
     */
    private java.awt.Color getAwtColor(Class objClass) {
        java.awt.Color col = colorsAwt.get(objClass);
        if (col == null) {
            // no color defined for this class
            return UNKNOWN_AWT_COLOR;
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
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Animal animal = field.getAnimalAt(row, col);
                Landscape landscape = field.getLandscapeAt(row, col);
                if (animal != null) {
                    // stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getAwtColor(animal.getClass()));
                } else {
                    fieldView.drawMark(col, row, getAwtColor(landscape.getClass()));
                }
            }
        }
        //stats.countFinished();

        //population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    private void setColorsForClasses() {
        setColor(Seal.class, Color.RED);
        setColor(PolarBear.class, Color.BLACK);
        setColor(Land.class, Color.LIGHTGREEN);
        setColor(Shallows.class, Color.CORAL);
        setColor(Shore.class, Color.LIGHTBLUE);
        setColor(Ocean.class, Color.AQUA);
        setAwtColor(Seal.class, java.awt.Color.RED);
        setAwtColor(PolarBear.class, java.awt.Color.BLUE);
        setAwtColor(Land.class, java.awt.Color.GREEN);
        setAwtColor(Shallows.class, java.awt.Color.RED);
        setAwtColor(Shore.class, java.awt.Color.BLACK);
        setAwtColor(Ocean.class, java.awt.Color.CYAN);
    }

    private void simulateOneStep() {
        sim.simulateOneStep();
        showStatus();
    }

    private void simulate(int steps) {
        for (int i = 0; i< steps; i++){
            sim.simulateOneStep();
            showStatus();
        }
    }
}
