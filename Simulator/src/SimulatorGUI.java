
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
    private Map<Class, Color> colors = new LinkedHashMap<>();;
    private static final Color UNKNOWN_COLOR = Color.GREY;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.root = createScene(primaryStage);
        this.sim = new Simulator();
        this.primaryStage = primaryStage;
        GridPane gp = new GridPane();
        createSimulatorWindow(primaryStage);
        gp.add(root, 0, 0);
        Scene scene = new Scene(gp, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("GAMES!");
        primaryStage.show();
    }

    private BorderPane createScene(Stage primaryStage) {

        BorderPane root = new BorderPane();
        HBox toolBar = new HBox();
        Button back = new Button("Back");
        back.setOnAction((ActionEvent event) -> {
            sim.simulateOneStep();
        });
        toolBar.getChildren().add(back);
        root.setTop(toolBar);
        Scene scene = new Scene(root, 450, 350);
        primaryStage.setScene(scene);
        return root;
    }

    private void createSimulatorWindow(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(0.7);
        gridPane.setHgap(0.7);
        root.setCenter(gridPane);
        setColor(Seal.class, Color.RED);
        setColor(PolarBear.class, Color.BLUE);
        setColor(Land.class, Color.GREEN);
        setColor(Shallows.class, Color.RED);
        setColor(Shore.class, Color.BLACK);
        setColor(Ocean.class, Color.CYAN);
        gridPane.setMaxHeight(Double.MAX_VALUE);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        createGrid(gridPane);
    }

    private void createGrid(GridPane gridPane) {
        for (int y = 0; y < depth; y++) {
            for (int x = 0; x < width; x++) {
                StackPane square = new StackPane();
                square.setMinHeight(5);
                square.setMinWidth(5);
                Field f = sim.getField();
                Object o = f.getObjectAt(y,x);
                System.out.println(o);
                Class c = o.getClass();
                Color col = getColor(c);
                
                square.setBackground(new Background(new BackgroundFill(col, CornerRadii.EMPTY, Insets.EMPTY)));

                square.setOnMouseClicked((MouseEvent event) -> {
                    System.out.println("dildo");
                });
                gridPane.add(square, x, y);
            }
        }
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
}
