
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
    private Stage primaryStage;
    private Simulator sim;
    private int depth = 80;
    private int width = 120;
    private Map<Class, Color> colors = new LinkedHashMap<>();
    ;
    private ArrayList<Rectangle> gridNodes = new ArrayList<>();
    private static final Color UNKNOWN_COLOR = Color.GREY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.root = createScene(primaryStage);
        this.sim = new Simulator();
        this.primaryStage = primaryStage;
        createSimulatorWindow(primaryStage);
        Scene scene = new Scene(root, 724, 512);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulator");
        primaryStage.show();
    }

    private BorderPane createScene(Stage primaryStage) {

        BorderPane borderPane = new BorderPane();
        HBox toolBar = new HBox();
        Button back = new Button("Step");
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
        borderPane.setTop(toolBar);
        return borderPane;
    }

    private void createSimulatorWindow(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(0.7);
        gridPane.setHgap(0.7);
        gridPane.setAlignment(Pos.CENTER);
        root.setCenter(gridPane);
        setObjectColors();
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
                    System.out.println(primaryStage.getHeight() + ", " + primaryStage.getWidth());
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

    private void showStatus() {
        gridNodes.forEach((Rectangle square) -> square.setFill(getColor(sim.getField().getObjectAt(new Location(square.getId())).getClass())));
    }

    private void setObjectColors() {
        setColor(Seal.class, Color.RED);
        setColor(PolarBear.class, Color.BLACK);
        setColor(Land.class, Color.LIGHTGREEN);
        setColor(Shallows.class, Color.CORAL);
        setColor(Shore.class, Color.LIGHTBLUE);
        setColor(Ocean.class, Color.AQUA);
    }

    private void simulateOneStep() {
        sim.simulateOneStep();
        Task task = new Task<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        showStatus();
                    }
                });
                System.out.println(Thread.currentThread());
                showStatus();
                return true;
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        try {
            th.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SimulatorGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void simulate(int steps) {
        for (int i = 0; i <= steps; i++) {
            simulateOneStep();
        }
    }
}
