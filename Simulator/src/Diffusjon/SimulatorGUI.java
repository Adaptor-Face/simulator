package Diffusjon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
    private DiffusjonSimulator sim;
    private int dimentions = 0;
    private final int defaultValue = 60;
    private int heigth = 1;
    private int width = 1;
    private int depth = 1;
    private int size = 20;
    private int txtSize = 10;
    private int particleNum = 1;
    private final HashMap<String, StackPane> gridNodes = new HashMap<>();
    private final HashMap<StackPane, Text> gridText = new HashMap<>();
    private final ArrayList<GridPane> thirdDimention = new ArrayList<>();
    private Text steps;
    private int step = 0;
    private final SimpleIntegerProperty obsStep = new SimpleIntegerProperty(0);
    private Stats stats;
    private final ScrollPane centerContent = new ScrollPane();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage alert = new Stage();
        GridPane gp = new GridPane();
        NumberField widthInput = new NumberField();
        widthInput.setPromptText("" + defaultValue);
        NumberField squareSize = new NumberField();
        squareSize.setPromptText("20");
        NumberField textSize = new NumberField();
        textSize.setPromptText("12");
        NumberField particles = new NumberField();
        particles.setPromptText("1");
        CheckBox oneD = new CheckBox();
        oneD.setSelected(true);
        CheckBox twoD = new CheckBox();
        twoD.setSelected(false);
        CheckBox threeD = new CheckBox();
        threeD.setSelected(false);
        Text wTxt = new Text("Width");
        Text oneDTxt = new Text("First Dimention");
        Text twoDTxt = new Text("Second Dimention");
        Text threeDTxt = new Text("Third Dimention");
        Text sqrSizeTxt = new Text("Square size");
        Text textSizeTxt = new Text("Text size");
        Text particlesTxt = new Text("Number of particles");
        gp.add(wTxt, 0, 0);
        gp.add(particlesTxt, 0, 1);
        gp.add(oneDTxt, 0, 2);
        gp.add(twoDTxt, 0, 3);
        gp.add(threeDTxt, 0, 4);
        gp.add(sqrSizeTxt, 0, 5);
        gp.add(textSizeTxt, 0, 6);
        gp.add(widthInput, 1, 0);
        gp.add(particles, 1, 1);
        gp.add(oneD, 1, 2);
        gp.add(twoD, 1, 3);
        gp.add(threeD, 1, 4);
        gp.add(squareSize, 1, 5);
        gp.add(textSize, 1, 6);
        gp.setId("");
        class EnterHandler implements EventHandler<KeyEvent> {

            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    int number = defaultValue;
                    try {
                        number = Integer.parseInt(widthInput.getText());
                    } catch (NumberFormatException ex) {
                    }
                    if (oneD.selectedProperty().getValue()) {
                        dimentions++;
                    }
                    if (twoD.selectedProperty().getValue()) {
                        dimentions++;
                    }
                    if (threeD.selectedProperty().getValue()) {
                        dimentions++;

                    }
                    if (dimentions >= 1) {
                        heigth = number;
                        width = number;
                        depth = number;
                    }
                    if (dimentions >= 2) {
                        width = number;
                    }
                    if (dimentions >= 3) {
                        depth = number;
                    }
                    if (!squareSize.getText().isEmpty()) {
                        size = Integer.parseInt(squareSize.getText());
                    }
                    if (!textSize.getText().isEmpty()) {
                        txtSize = Integer.parseInt(textSize.getText());
                    }
                    if (!particles.getText().isEmpty()) {
                        particleNum = Integer.parseInt(particles.getText());
                    }
                    alert.close();
                }
            }
        }
        Button start = new Button("Start Simulation");
        start.setOnAction((ActionEvent e) -> {
            int number = defaultValue;
            try {
                number = Integer.parseInt(widthInput.getText());
            } catch (NumberFormatException ex) {
            }
            int dimentions = 0;
            if (oneD.selectedProperty().getValue()) {
                dimentions++;
            }
            if (twoD.selectedProperty().getValue()) {
                dimentions++;
            }
            if (threeD.selectedProperty().getValue()) {
                dimentions++;

            }
            if (dimentions >= 1) {
                width = number;
            }
            if (dimentions >= 2) {
                heigth = number;
            }
            if (dimentions >= 3) {
                depth = number;
            }
            if (!squareSize.getText().isEmpty()) {
                size = Integer.parseInt(squareSize.getText());
            }
            if (!textSize.getText().isEmpty()) {
                txtSize = Integer.parseInt(textSize.getText());
            }
            if (!particles.getText().isEmpty()) {
                particleNum = Integer.parseInt(particles.getText());
            }
            alert.close();
        });
        alert.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent w) {
                gp.setId("close");
            }
        });
        start.setOnKeyPressed(new EnterHandler());
        widthInput.setOnKeyPressed(new EnterHandler());
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BOTTOM_RIGHT);
        vBox.getChildren().add(gp);
        vBox.getChildren().add(start);
        alert.setScene(new Scene(vBox, 250, 180));
        start.requestFocus();
        alert.setTitle("Simulator");
        alert.showAndWait();
        if (!gp.getId().equals("close")) {
            sim = new DiffusjonSimulator(particleNum, width / 2, dimentions);
            gp.setHgap(5);
            gp.setVgap(5);
            this.root = createScene(primaryStage);
            this.primaryStage = primaryStage;
            this.steps = new Text("Steps: " + step);
            root.setCenter(createSimulatorWindow(primaryStage));
            obsStep.addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue ov, Number oldValue, Number newValue) {
//                    Stats.stepLog(population.getText());
//                    if (newValue.intValue() <= oldValue.intValue()) {
//                        Stats.endLog();
//                    } else {
//                    }
                }
            });
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
//        Scene controlScene = new Scene(root, 500, 75);
//        primaryStage.setScene(controlScene);
            primaryStage.setTitle("Simulator");
            primaryStage.show();
        }
    }

    private BorderPane createScene(Stage primaryStage) {

        BorderPane borderPane = new BorderPane();
        HBox toolBar = new HBox();
        Button back = new Button("One Step");
        back.setOnAction((ActionEvent event) -> {
            simulateOneStep();
//            System.out.println(sim.getField().lookFor(new Location(15, 67), Shallows.class, "NSEW"));
        });
        back.setOnKeyPressed((KeyEvent event) -> {
            simulateOneStep();
        });
        NumberField stepInput = new NumberField();
        stepInput.setPromptText("Steps");
        Button multiStep = new Button("Step");
        multiStep.setOnAction((ActionEvent event) -> {
            if (stepInput.getText().length() > 0) {
                simulate(Integer.parseInt(stepInput.getText()));
            }
        });
        Button reset = new Button("Reset");
        reset.setOnAction((ActionEvent event) -> {
            reset();
        });
        NumberField plane = new NumberField();
        plane.setPromptText("Plane");
        Button planeSwap = new Button("Swap plane");
        planeSwap.setOnAction((ActionEvent event) -> {
            if (planeSwap.getText().length() > 0) {
                centerContent.setContent(thirdDimention.get(Integer.parseInt(plane.getText())));
            } else {
            }
        });
        toolBar.getChildren().add(back);
        toolBar.getChildren().add(stepInput);
        toolBar.getChildren().add(multiStep);
        toolBar.getChildren().add(reset);
        toolBar.getChildren().add(plane);
        toolBar.getChildren().add(planeSwap);
        borderPane.setTop(toolBar);
        return borderPane;
    }

    private BorderPane createSimulatorWindow(Stage stage) {
        BorderPane borderPane = new BorderPane();
        centerContent.setStyle("-fx-font-size: 20px;");
        createGrid();
        System.out.println(thirdDimention.size());
        System.out.println(thirdDimention.size() / 2);
        centerContent.setContent(thirdDimention.get(thirdDimention.size() / 2));
        centerContent.setMinHeight(400);
        centerContent.setMaxHeight(800);
        centerContent.setMinWidth(600);
        centerContent.setMaxWidth(1200);
        borderPane.setCenter(centerContent);
        borderPane.setTop(steps);
        return borderPane;
    }

    private void createGrid() {
        for (int z = 0; z < depth; z++) {
            GridPane gridPane = new GridPane();
            gridPane.setVgap(0.7);
            gridPane.setHgap(0.7);
            gridPane.setAlignment(Pos.CENTER);
            gridPane.setMaxHeight(Double.MAX_VALUE);
            gridPane.setMaxWidth(Double.MAX_VALUE);
            gridPane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));
            for (int y = 0; y < heigth; y++) {
                for (int x = 0; x < width; x++) {
                    StackPane square = new StackPane();
                    Text txt = new Text("" + z);
                    txt.setFill(Color.WHITE);
                    txt.setStyle("-fx-font-size: " + txtSize + "px;");
                    square.getChildren().add(txt);
                    square.setMinHeight(size / 2);
                    square.setMinWidth(size);
                    GridPane.setHgrow(square, Priority.ALWAYS);
                    GridPane.setVgrow(square, Priority.ALWAYS);
                    gridPane.add(square, x, y);
                    gridNodes.put(x + "," + y + "," + z, square);
                    gridText.put(square, txt);
                }
            }
            thirdDimention.add(gridPane);
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

    private void reset() {
        step = 0;
        obsStep.set(0);
        System.out.println(obsStep);
        setSqauresColor("#444444");
        showStatus();

    }

    private void showStatus() {
        setSqauresColor("#444444");
        this.steps.setText("Steps: " + step);
    }

//    private void setObjectColors() {
//        setColor(Seal.class, Color.RED);
//        setColor(PolarBear.class, Color.BLACK);
//        setColor(Land.class, Color.AZURE);
//        setColor(Shallows.class, Color.AQUA);
//        setColor(Shore.class, Color.LIGHTBLUE);
//        setColor(Ocean.class, Color.CORNFLOWERBLUE);
//    }
    private void simulateOneStep() {
        ArrayList<Location> particles = new ArrayList<>();
        particles.addAll(sim.simulateOneStep(dimentions));
        particles.forEach((Location loc) -> {
            setColor(loc.toString(), "#FFFFFF");
        });
        particles.clear();
        step++;
//        sim.simulateOneStep();
        obsStep.setValue(step);
        showStatus();
    }

    private void simulate(int steps) {
        Task<Integer> task = new Task<Integer>() {
            @Override
            public Integer call() throws Exception {
                for (Integer i = 0; i < steps; i++) {
                    simulateOneStep();
                    updateValue(sim.getStep());
                    step++;
                    obsStep.setValue(step);
                }
                return null;
            }
        };
        task.valueProperty().addListener((obs, oldStep, newStep) -> {
            if (oldStep == null) {
                oldStep = 0;
            }
            if (newStep == null) {
                newStep = oldStep + 1;
            }
            if (oldStep == newStep - 1) {
            }
        });
        new Thread(task).start();
        showStatus();
    }

    private void setColor(String location, String hexColor) {
        gridNodes.get(location).setBackground(new Background(new BackgroundFill(Color.web(hexColor), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void setSqauresColor(String hexColor) {
        gridNodes.values().forEach((StackPane square) -> {
            square.setBackground(new Background(new BackgroundFill(Color.web(hexColor), CornerRadii.EMPTY, Insets.EMPTY)));
        });
    }

    private class NumberField extends TextField {

        public NumberField() {
            super();
            this.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                try {
                    if (newValue.length() > 0 && newValue.length() <= 8) {
                        Integer.parseInt(newValue);
                    } else if (newValue.length() == 0) {
                        this.setText("");
                    } else {
                        this.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    this.setText(oldValue);
                }
            });
        }
    }
}
