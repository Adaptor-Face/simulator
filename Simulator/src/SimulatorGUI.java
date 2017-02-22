
import com.sun.corba.se.impl.orbutil.graph.Graph;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
    private ArrayList<Rectangle> gridNodes = new ArrayList<>();
    private static final Color UNKNOWN_COLOR = Color.GREY;
    private Text population;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.root = createScene(primaryStage);
        this.sim = new Simulator();
        this.primaryStage = primaryStage;
        root.setCenter(createSimulatorWindow(primaryStage));
        Scene scene = new Scene(root, 724, 512);
        primaryStage.setScene(scene);
//        Scene controlScene = new Scene(root, 500, 75);
//        primaryStage.setScene(controlScene);
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
            sim.softReset();
            showStatus();
        });
        toolBar.getChildren().add(back);
        toolBar.getChildren().add(stepInput);
        toolBar.getChildren().add(multiStep);
        toolBar.getChildren().add(reset);
        borderPane.setTop(toolBar);
        return borderPane;
    }

    private BorderPane createSimulatorWindow(Stage stage) {
        GridPane gridPane = new GridPane();
        BorderPane borderPane = new BorderPane();
        gridPane.setVgap(0.7);
        gridPane.setHgap(0.7);
        gridPane.setAlignment(Pos.CENTER);
        setObjectColors();
        gridPane.setMaxHeight(Double.MAX_VALUE);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        createGrid(gridPane, stage);
        borderPane.setCenter(gridPane);
        
        
        return borderPane;
    }

    private void createGrid(GridPane gridPane, Stage currentStage) {
        for (int y = 0; y < depth; y++) {
            for (int x = 0; x < width; x++) {
                Rectangle square = new Rectangle(5, 5, UNKNOWN_COLOR);
                square.setId(new Location(y, x).toString());
                square.setOnMouseEntered((MouseEvent event) -> {
                    Animal animal = sim.getField().getAnimalAt(new Location(square.getId()));
                    if (animal != null) {
                        Tooltip tt = new Tooltip();
                        String text = "";
                        for (String string : animal.getAnimalDetails()) {
                            text += string + "\n";
                        }
                        text += square.getId();
                        tt.setText(text);
                        Tooltip.install(square, tt);
                    }
                });
                square.setOnMouseClicked((MouseEvent event) -> {
                    Animal animal = sim.getField().getAnimalAt(new Location(square.getId()));
                    if (animal != null) {
                        Stage alert = new Stage();
                        VBox vBox = new VBox();
                        for (String string : animal.getAnimalDetails()) {
                            vBox.getChildren().add(new Text(string));
                        }
                        alert.setScene(new Scene(vBox, 50, 100));
                        alert.showAndWait();
                        System.out.println(animal.getAnimalDetails());
                        currentStage.close();
                    }
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
        Field someField = new Field(sim.getField().getDepth(), sim.getField().getWidth());
        someField.resetField(sim.getField());
        gridNodes.forEach((Rectangle square) -> {
            square.setFill(getColor(someField.getObjectAt(new Location(square.getId())).getClass()));
        });
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
        showStatus();
    }

    private void simulate(int steps) {
        Task<Integer> task = new Task<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (Integer i = 0; i <= steps; i++) {
                    sim.simulateOneStep();
                    updateValue(sim.getStep());
                }
                return null;
            }
        };
        task.valueProperty().addListener((obs, oldStep, newStep) -> {
            if(oldStep == null){
                oldStep = 0;
            }
            if(newStep == null){
                newStep = oldStep +1;
            }
                if (oldStep == newStep - 1) {
                    Field field = sim.getField();
                    gridNodes.forEach((Rectangle square) -> {
                        Object obj = field.getObjectAt(new Location(square.getId()));
                        Color col = getColor(obj.getClass());
                        square.setFill(col);
                    });
                }
        });
        new Thread(task).start();
    }
}
