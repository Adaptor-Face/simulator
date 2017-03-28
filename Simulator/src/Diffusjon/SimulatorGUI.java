package Diffusjon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    private int height = 1;
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
    private int visualType;
    private IntegerProperty currentPlane;
    private final ArrayList<IntegerProperty> takenPlanes = new ArrayList<>();
    private boolean heatMap = false;
    private final ArrayList<Stage> alerts = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        GridPane gp = createStartupAlert();
        if (!gp.getId().equals("close")) {
            createMainWindow();
        }
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
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    StackPane square = new StackPane();
                    Text txt = new Text();
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
        sim.reset();
        step = 0;
        obsStep.set(0);
        setSqauresColor(40, 40, 40);
        showStatus();

    }

    private void showStatus() {
        if (visualType > 0) {
            gridText.values().forEach((Text txt) -> {
                txt.setText("");
            });
        }
        setSqauresColor(40, 40, 40);
        ArrayList<Location> particles = new ArrayList<>();
        particles.addAll(sim.getLocs());
        particles.forEach((Location loc) -> {
            updateVisuals(loc.toString(), "#FFFFFF");
        });
        this.steps.setText("Steps: " + step);
        particles.clear();
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
        sim.simulateOneStep(dimentions);
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
                    sim.simulateOneStep(dimentions);
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
                ArrayList<Location> particles = new ArrayList<>();
                particles.addAll(sim.getLocs());
                particles.forEach((Location loc) -> {
                    updateVisuals(loc.toString(), "#FFFFFF");
                });
                obsStep.setValue(step);
            }
        });
        Thread t1 = new Thread(task);
        t1.start();
        showStatus();
    }

    private void updateVisuals(String location, String hexColor) {
        if (visualType == 0) {
            gridNodes.get(location).setBackground(new Background(new BackgroundFill(Color.web(hexColor), CornerRadii.EMPTY, Insets.EMPTY)));
        } else if (visualType == 1) {
            int num;
            try {
                num = Integer.parseInt(gridText.get(gridNodes.get(location)).getText());
            } catch (NumberFormatException ex) {
                num = 0;
            }
            num++;
            gridText.get(gridNodes.get(location)).setText("" + num);
        }
    }

    private void setSqauresColor(int r, int g, int b) {
        gridNodes.keySet().forEach((String loc) -> {
            StackPane square = gridNodes.get(loc);
            int bFinished = b;
            if (heatMap) {
                String[] str = loc.split(",");
                Location middle = new Location(width / 2, height / 2, depth / 2);
//            int bFinished = b + (Integer.parseInt(str[0]) - middle.getX()) + (Integer.parseInt(str[1]) - middle.getY()) + (Integer.parseInt(str[2]) - middle.getZ());
                int num1 = Math.abs(Integer.parseInt(str[0]) - middle.getX());
                int num2 = Math.abs(Integer.parseInt(str[1]) - middle.getY());
                int num3 = Math.abs(Integer.parseInt(str[2]) - middle.getZ());
                bFinished = 255 - ((num1 + num2 + num3) * (300 / width));
                if (bFinished < 40) {
                    bFinished = 40;
                }
                if (bFinished > 255) {
                    bFinished = 255;
                }
            }
            String hexColor = "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(bFinished);
            square.setBackground(new Background(new BackgroundFill(Color.web(hexColor), CornerRadii.EMPTY, Insets.EMPTY)));
        });
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
        final NumberField plane = new NumberField();
        plane.setPromptText("Plane");
        currentPlane = new SimpleIntegerProperty(depth / 2);
        takenPlanes.add(currentPlane);
        plane.setText(new Integer(depth / 2).toString());
        Button planeSwap = new Button("Swap plane");
        planeSwap.setOnAction((ActionEvent event) -> {
            if (plane.getText().length() > 0) {
                currentPlane.setValue(Integer.parseInt(plane.getText()));
                plane.setText("" + currentPlane);
            } else {
                plane.setText("" + currentPlane);
            }
        });
        final Button planeUp = new Button("Go up");
        planeUp.setOnAction((ActionEvent event) -> {
            currentPlane.set(currentPlane.intValue() + 1);
        });
        final Button planeDown = new Button("Go Down");
        planeDown.setOnAction((ActionEvent event) -> {
            currentPlane.set(currentPlane.intValue() - 1);
        });
        currentPlane.addListener((obs, ov, nv) -> {
            if (nv.intValue() >= 0 && nv.intValue() < depth) {
                centerContent.setContent(thirdDimention.get(nv.intValue()));
                plane.setText(nv.toString());
            } else if (nv.intValue() < 0) {
                currentPlane.set(depth - 1);
            } else if (nv.intValue() > depth - 1) {
                currentPlane.set(0);
            }
        });
        Button planeView = new Button("View anotherPlane");
        planeView.setOnAction((ActionEvent event) -> {
            if (takenPlanes.size() < depth) {
                Stage alert = new Stage();
                VBox vBox = new VBox();
                ArrayList<Integer> available = new ArrayList<>();
                for (int i = 0; i < depth; i++) {
                    available.add(i);
                }
                takenPlanes.forEach((IntegerProperty e) -> {
                    available.remove(new Integer(e.intValue()));
                });
                ScrollPane windowContent = new ScrollPane();
                windowContent.setContent(thirdDimention.get(available.get(0)));
                NumberField plane2 = new NumberField();
                plane2.setPromptText("Plane");
                IntegerProperty currentPlane2 = new SimpleIntegerProperty(available.get(0));
                takenPlanes.add(currentPlane2);
                plane2.setText(Integer.toString(available.get(0)));
                available.remove(0);
                Button planeSwap2 = new Button("Swap plane");
                planeSwap2.setOnAction((ActionEvent event2) -> {
                    if (plane.getText().length() > 0) {
                        currentPlane2.setValue(Integer.parseInt(plane2.getText()));
                        plane2.setText("" + currentPlane2);
                    } else {
                        plane2.setText("" + currentPlane2);
                    }
                });
                Button planeUp2 = new Button("Go up");
                planeUp2.setOnAction((ActionEvent event2) -> {
                    currentPlane2.set(currentPlane2.intValue() + 1);
                });
                Button planeDown2 = new Button("Go Down");
                planeDown2.setOnAction((ActionEvent event2) -> {
                    currentPlane2.set(currentPlane2.intValue() - 1);
                });
                currentPlane2.addListener((obs, ov, nv) -> {
                    available.clear();
                    for (int i = 0; i < depth; i++) {
                        available.add(i);
                    }
                    takenPlanes.forEach((IntegerProperty e) -> {
                        if (e != currentPlane2) {
                            available.remove(new Integer(e.intValue()));
                        }
                    });
                    if (nv.intValue() >= 0 && nv.intValue() <= depth) {
                        while (!available.contains(nv.intValue())) {
                            nv = nv.intValue() + 1;
                            if (nv.intValue() > depth - 1) {
                                nv = 0;
                            }
                        }
                        windowContent.setContent(thirdDimention.get(nv.intValue()));
                        plane2.setText(nv.toString());
                    } else if (nv.intValue() < 0) {
                        currentPlane2.set(depth - 1);
                    } else if (nv.intValue() > depth - 1) {
                        currentPlane2.set(0);
                    }
                });
                HBox tools = new HBox();
                tools.getChildren().add(plane2);
                tools.getChildren().add(planeSwap2);
                tools.getChildren().add(planeUp2);
                tools.getChildren().add(planeDown2);
                vBox.getChildren().add(tools);
                vBox.getChildren().add(windowContent);
                alert.setScene(new Scene(vBox));
                alert.setOnCloseRequest((WindowEvent e) -> {
                    takenPlanes.remove(currentPlane2);
                    alerts.remove(alert);
                });
                alerts.add(alert);
                alert.showAndWait();
            }
        });
        Button newSim = new Button("Restart");
        newSim.setOnAction((ActionEvent event) -> {
            primaryStage.close();
            dimentions = 0;
            gridNodes.clear();
            gridText.clear();
            takenPlanes.clear();
            thirdDimention.clear();
            GridPane gp = createStartupAlert();
            if (!gp.getId().equals("close")) {
                createMainWindow();
                System.out.println(depth);
                System.out.println(thirdDimention.size());
            }
        });
        newSim.setAlignment(Pos.BASELINE_RIGHT);
        if (depth == 1) {
            plane.setDisable(true);
            planeSwap.setDisable(true);
            planeUp.setDisable(true);
            planeDown.setDisable(true);
            planeView.setDisable(true);
        }
        toolBar.getChildren().add(back);
//        toolBar.getChildren().add(stepInput);
//        toolBar.getChildren().add(multiStep);
        toolBar.getChildren().add(reset);
        toolBar.getChildren().add(plane);
        toolBar.getChildren().add(planeSwap);
        toolBar.getChildren().add(planeUp);
        toolBar.getChildren().add(planeDown);
        toolBar.getChildren().add(planeView);
        toolBar.getChildren().add(newSim);

        borderPane.setTop(toolBar);
        return borderPane;
    }

    private BorderPane createSimulatorWindow(Stage stage) {
        HBox subToolBar = new HBox();
        subToolBar.setSpacing(15);
        CheckBox heatMap = new CheckBox();
        heatMap.setText("Show heat map");
        heatMap.setOnAction((ActionEvent a) -> {
            this.heatMap = heatMap.isSelected();
            showStatus();
        });
        ToggleGroup moveSelect = new ToggleGroup();
        RadioButton rook = new RadioButton();
        rook.setSelected(true);
        rook.setText("Rook");
        rook.setToggleGroup(moveSelect);
        rook.setUserData(0);
        RadioButton bishop = new RadioButton();
        bishop.setSelected(false);
        bishop.setText("Bishop");
        bishop.setToggleGroup(moveSelect);
        bishop.setUserData(1);
        RadioButton king = new RadioButton();
        king.setSelected(false);
        king.setText("King");
        king.setToggleGroup(moveSelect);
        king.setUserData(2);
        RadioButton knight = new RadioButton();
        knight.setSelected(false);
        knight.setText("Knight");
        knight.setToggleGroup(moveSelect);
        knight.setUserData(3);
        HBox radioButtons = new HBox();
        Text txt = new Text("Move type:");
        radioButtons.getChildren().add(txt);
        radioButtons.getChildren().add(rook);
        radioButtons.getChildren().add(bishop);
        radioButtons.getChildren().add(king);
        radioButtons.getChildren().add(knight);
        moveSelect.selectedToggleProperty().addListener((obs, ol, nl) -> {
            sim.setMoves((int) nl.getUserData());
        });
        BorderPane borderPane = new BorderPane();
        centerContent.setStyle("-fx-font-size: 20px;");
        createGrid();
        centerContent.setContent(thirdDimention.get(thirdDimention.size() / 2));
        centerContent.setMinHeight(400);
        centerContent.setMinWidth(600);
        borderPane.setCenter(centerContent);
        subToolBar.getChildren().add(steps);
        subToolBar.getChildren().add(heatMap);
        subToolBar.getChildren().add(radioButtons);
        borderPane.setTop(subToolBar);
        return borderPane;
    }

    private GridPane createStartupAlert() {
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
        HBox choiceBox = new HBox();
        ToggleGroup choice = new ToggleGroup();
        RadioButton color = new RadioButton();
        color.setToggleGroup(choice);
        color.setSelected(true);
        color.setText("Color");
        color.setId("0");
        RadioButton count = new RadioButton();
        count.setToggleGroup(choice);
        count.setText("Count");
        count.setId("1");
        RadioButton decimal = new RadioButton();
        decimal.setToggleGroup(choice);
        decimal.setText("Decimal");
        decimal.setId("2");
        choiceBox.getChildren().add(color);
        choiceBox.getChildren().add(count);
        choiceBox.getChildren().add(decimal);
        Text wTxt = new Text("Width");
        Text oneDTxt = new Text("First Dimention");
        Text twoDTxt = new Text("Second Dimention");
        Text threeDTxt = new Text("Third Dimention");
        Text sqrSizeTxt = new Text("Square size");
        Text textSizeTxt = new Text("Text size");
        Text particlesTxt = new Text("Number of particles");
        Text choiceText = new Text("Visual type");
        gp.add(wTxt, 0, 0);
        gp.add(particlesTxt, 0, 1);
        gp.add(oneDTxt, 0, 2);
        gp.add(twoDTxt, 0, 3);
        gp.add(threeDTxt, 0, 4);
        gp.add(sqrSizeTxt, 0, 5);
        gp.add(textSizeTxt, 0, 6);
        gp.add(choiceText, 0, 7);
        gp.add(widthInput, 1, 0);
        gp.add(particles, 1, 1);
        gp.add(oneD, 1, 2);
        gp.add(twoD, 1, 3);
        gp.add(threeD, 1, 4);
        gp.add(squareSize, 1, 5);
        gp.add(textSize, 1, 6);
        gp.add(choiceBox, 1, 7);
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
                        width = number;
                    }
                    if (dimentions >= 2) {
                        height = number;
                    } else {
                        height = 1;
                    }
                    if (dimentions >= 3) {
                        depth = number;
                    } else {
                        depth = 1;
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
                height = number;
            } else {
                height = 1;
            }
            if (dimentions >= 3) {
                depth = number;
            } else {
                depth = 1;
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
        alert.setScene(new Scene(vBox, 300, 200));
        start.requestFocus();
        alert.setTitle("Simulator");
        alert.showAndWait();
        visualType = Integer.parseInt(((RadioButton) choice.getSelectedToggle()).getId());
        return gp;
    }

    private void createMainWindow() {
        sim = new DiffusjonSimulator(particleNum, width, dimentions);
//            gp.setHgap(5);
//            gp.setVgap(5);
        this.root = createScene(primaryStage);
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
        primaryStage.setOnCloseRequest(eh -> {
            ArrayList<Stage> close = new ArrayList<>(alerts);
            close.forEach(cnsmr -> {
                cnsmr.close();
            });
        });
        primaryStage.show();
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
