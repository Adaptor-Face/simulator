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
    private int heigth = 80;
    private int width = 120;
    private int depth = 1;
    private HashMap<String, StackPane> gridNodes = new HashMap<>();
    private ArrayList<GridPane> thirdDimention = new ArrayList<>();
    private static final Color UNKNOWN_COLOR = Color.GREY;
    private Text steps;
    private int step = 0;
    private SimpleIntegerProperty obsStep = new SimpleIntegerProperty(0);
    private Stats stats;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage alert = new Stage();
        GridPane gp = new GridPane();
        NumberField widthInput = new NumberField();
        widthInput.setPromptText("120");
        NumberField heigthInput = new NumberField();
        heigthInput.setPromptText("80");
        NumberField depthInput = new NumberField();
        depthInput.setPromptText("1");

        Text wTxt = new Text("Width");
        Text hTxt = new Text("Height");
        Text sTxt = new Text("Depth");
        gp.add(wTxt, 0, 0);
        gp.add(hTxt, 0, 1);
        gp.add(sTxt, 0, 2);
        gp.add(widthInput, 1, 0);
        gp.add(heigthInput, 1, 1);
        gp.add(depthInput, 1, 2);
        gp.setId("");
        class EnterHandler implements EventHandler<KeyEvent> {

            @Override
            public void handle(KeyEvent k) {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    if (widthInput.getText().length() > 0) {
                        width = Integer.parseInt(widthInput.getText());
                    }
                    if (heigthInput.getText().length() > 0) {
                        heigth = Integer.parseInt(heigthInput.getText());
                    }
                    if (depthInput.getText().length() > 0) {
                        depth = Integer.parseInt(depthInput.getText());
                    }
                    alert.close();
                }
            }
        }
        Button start = new Button("Start Simulation");
        start.setOnAction((ActionEvent e) -> {
            if (widthInput.getText().length() > 0) {
                width = Integer.parseInt(widthInput.getText());
            }
            if (heigthInput.getText().length() > 0) {
                heigth = Integer.parseInt(heigthInput.getText());
            }
            if (depthInput.getText().length() > 0) {
                depth = Integer.parseInt(depthInput.getText());
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
        heigthInput.setOnKeyPressed(new EnterHandler());
        depthInput.setOnKeyPressed(new EnterHandler());
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.BOTTOM_RIGHT);
        vBox.getChildren().add(gp);
        vBox.getChildren().add(start);
        alert.setScene(new Scene(vBox, 250, 120));
        start.requestFocus();
        alert.setTitle("Simulator");
        alert.showAndWait();
        if (!gp.getId().equals("close")) {
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
        Button getStats = new Button("Get stats");
        getStats.setOnAction((ActionEvent event) -> {
            Stage alert = new Stage();
            VBox vBox = new VBox();
            Text info = new Text("SOME TEXT");
            vBox.getChildren().add(info);
            alert.setScene(new Scene(vBox));
            alert.showAndWait();

        });
        toolBar.getChildren().add(back);
        toolBar.getChildren().add(stepInput);
        toolBar.getChildren().add(multiStep);
        toolBar.getChildren().add(reset);
        toolBar.getChildren().add(getStats);
        borderPane.setTop(toolBar);
        return borderPane;
    }

    private BorderPane createSimulatorWindow(Stage stage) {
        GridPane gridPane = new GridPane();
        BorderPane borderPane = new BorderPane();
        gridPane.setVgap(0.7);
        gridPane.setHgap(0.7);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setMaxHeight(Double.MAX_VALUE);
        gridPane.setMaxWidth(Double.MAX_VALUE);
        createGrid(gridPane, stage);
        borderPane.setCenter(gridPane);
        borderPane.setTop(steps);
        return borderPane;
    }

    private void createGrid(GridPane gridPane, Stage currentStage) {
        for (int y = 0; y < heigth; y++) {
            for (int x = 0; x < width; x++) {
                StackPane square = new StackPane();
                square.setMinHeight(4);
                square.setMinWidth(4);
                GridPane.setHgrow(square, Priority.ALWAYS);
                GridPane.setVgrow(square, Priority.ALWAYS);
                gridPane.add(square, x, y);
                gridNodes.put(x + "," + y, square);
            }
        }
        gridPane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));
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
        setParticleColor("#222222");
        showStatus();

    }

    private void showStatus() {
        setParticleColor("#222222");
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
//                    sim.simulateOneStep();
//                    updateValue(sim.getStep());
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
                setParticleColor("#222222");
            }
        });
        new Thread(task).start();
        showStatus();
    }

    private void setParticleColor(String hexColor) {
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
