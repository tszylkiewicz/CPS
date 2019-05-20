package sample.Controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Signals.*;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static javafx.collections.FXCollections.observableArrayList;

public class GenerateSignalController implements Initializable {

    //Choice boxes
    public ChoiceBox<String> signalType;
    public ChoiceBox<String> generationChart;
    public ChoiceBox<String> operationType;
    public ChoiceBox<String> firstElement;
    public ChoiceBox<String> secondElement;
    public ChoiceBox<String> result;

    //Parameters
    public TextField amplitude;         //A
    public TextField startingPoint;     //t1
    public TextField duration;          //d
    public TextField period;            //T
    public TextField dutyCycle;         //kw
    public TextField step;              //ts
    public TextField frequency;         //f
    public TextField probability;       //p
    public TextField sampleNumber;      //ns


    //Filtration
    public TextField filterMagnitude;
    public TextField filterCoefficient;
    public ChoiceBox<String> windowType;
    public ChoiceBox<String> filterType;

    public CheckBox correlationType;
    //Grids with average values
    public GridPane grid1;
    public GridPane grid2;
    public GridPane grid3;

    //Line charts
    public LineChart<Double, Double> lineChart1;
    public LineChart<Double, Double> lineChart2;
    public LineChart<Double, Double> lineChart3;

    //ScatterCharts
    public ScatterChart<Double, Double> scatterChart1;
    public ScatterChart<Double, Double> scatterChart2;
    public ScatterChart<Double, Double> scatterChart3;

    //Signal chosen in choice box
    private Object chosenSignal;

    //Signals
    private BaseSignal[] signal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialize choice boxes
        signalType.setItems(signalList);
        generationChart.setItems(chartList);
        operationType.setItems(operationList);
        firstElement.setItems(chartList);
        secondElement.setItems(chartList);
        result.setItems(chartList);
        windowType.setItems(windowList);
        filterType.setItems(filterList);

        UnaryOperator<TextFormatter.Change> filter = t -> {

            if (t.isReplaced())
                if (t.getText().matches("[^0-9]"))
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


            if (t.isAdded()) {
                if (t.getControlText().contains("-")) {
                    if (t.getText().matches("[^0-9.]")) {
                        t.setText("");
                    }
                }
                if (t.getControlText().contains(".")) {
                    if (t.getText().matches("[^0-9]")) {
                        t.setText("");
                    }
                } else if (t.getText().matches("[^0-9.-]")) {
                    t.setText("");
                }
            }

            return t;
        };

        amplitude.setTextFormatter(new TextFormatter<>(filter));
        startingPoint.setTextFormatter(new TextFormatter<>(filter));
        duration.setTextFormatter(new TextFormatter<>(filter));
        period.setTextFormatter(new TextFormatter<>(filter));
        dutyCycle.setTextFormatter(new TextFormatter<>(filter));
        step.setTextFormatter(new TextFormatter<>(filter));
        frequency.setTextFormatter(new TextFormatter<>(filter));
        probability.setTextFormatter(new TextFormatter<>(filter));
        sampleNumber.setTextFormatter(new TextFormatter<>(filter));
        filterMagnitude.setTextFormatter(new TextFormatter<>(filter));
        filterCoefficient.setTextFormatter(new TextFormatter<>(filter));

        setParametersVisibility(false, false, false, false, false, false, false, false, false);

        lineChart1.setVisible(true);
        lineChart2.setVisible(true);
        lineChart3.setVisible(true);
        scatterChart1.setVisible(false);
        scatterChart2.setVisible(false);
        scatterChart3.setVisible(false);

        signal = new BaseSignal[3];
    }

    @FXML
    private void chooseSignal() {
        chosenSignal = signalType.getValue();
        if (chosenSignal == null) {
            Error("Signal generator error", "No signal chosen.");
        } else {
            //A, t1, d
            if (chosenSignal == "Uniform noise" || chosenSignal == "Gaussian noise") {
                setParametersVisibility(true, true, true, false, false, false, false, false, false);
                //A, T, t1, d
            } else if (chosenSignal == "Sine wave" || chosenSignal == "Half-wave rectified sine" || chosenSignal == "Full-wave rectifier sine") {
                setParametersVisibility(true, true, true, true, false, false, false, false, false);
                //A, T, t1, d, kw
            } else if (chosenSignal == "Square wave" || chosenSignal == "Symmetrical square wave" || chosenSignal == "Triangle wave") {
                setParametersVisibility(true, true, true, true, true, false, false, false, false);
                //A, t1, d, ts
            } else if (chosenSignal == "Unit step function") {
                setParametersVisibility(true, true, true, false, false, true, false, false, false);
                //A, t1, d, f, ns
            } else if (chosenSignal == "Kronecker impulse") {
                setParametersVisibility(true, true, true, false, false, false, true, false, true);
                //A, t1, d, f, p
            } else if (chosenSignal == "Impulse noise") {
                setParametersVisibility(true, true, true, false, false, false, true, true, false);
            }
        }
    }

    @FXML
    private void generateSignal() {
        if (generationChart == null) {
            Error("Chart error", "Chart has to be selected");
        } else {
            if (validateParameter()) {
                float A = 0, T = 0, t1 = 0, d = 0, kw = 0, ts = 0, f = 0, ns = 0, p = 0;
                if (amplitude.isVisible()) {
                    A = Float.parseFloat(amplitude.getText());
                }
                if (startingPoint.isVisible()) {
                    t1 = Float.parseFloat(startingPoint.getText());
                }
                if (period.isVisible()) {
                    T = Float.parseFloat(period.getText());
                }
                if (duration.isVisible()) {
                    d = Float.parseFloat(duration.getText());
                }
                if (dutyCycle.isVisible()) {
                    kw = Float.parseFloat(dutyCycle.getText());
                }
                if (step.isVisible()) {
                    ts = Float.parseFloat(step.getText());
                }
                if (frequency.isVisible()) {
                    f = Float.parseFloat(frequency.getText());
                }
                if (probability.isVisible()) {
                    p = Float.parseFloat(probability.getText());
                }
                if (sampleNumber.isVisible()) {
                    ns = Float.parseFloat(sampleNumber.getText());
                }

                int i = 0;
                if (generationChart.getValue().equals("Chart 2")) {
                    i = 1;
                } else if (generationChart.getValue().equals("Chart 3")) {
                    i = 2;
                }

                if (chosenSignal == "Uniform noise") {
                    signal[i] = new UniformNoise(A, t1, d, 0);
                }
                if (chosenSignal == "Gaussian noise") {
                    signal[i] = new UniformNoise(A, t1, d, 1);
                }
                if (chosenSignal == "Sine wave") {
                    signal[i] = new SineWave(A, t1, d, T, 0);
                }
                if (chosenSignal == "Half-wave rectified sine") {
                    signal[i] = new SineWave(A, t1, d, T, 1);
                }
                if (chosenSignal == "Full-wave rectifier sine") {
                    signal[i] = new SineWave(A, t1, d, T, 2);
                }
                if (chosenSignal == "Square wave") {
                    signal[i] = new SquareWave(A, t1, d, T, kw, 0);
                }
                if (chosenSignal == "Symmetrical square wave") {
                    signal[i] = new SquareWave(A, t1, d, T, kw, 1);
                }
                if (chosenSignal == "Triangle wave") {
                    signal[i] = new SquareWave(A, t1, d, T, kw, 2);
                }
                if (chosenSignal == "Unit step function") {
                    signal[i] = new UnitStep(A, t1, d, ts);
                }
                if (chosenSignal == "Kronecker impulse") {
                    signal[i] = new KroneckerImpulse(A, t1, d, f, ns);
                }
                if (chosenSignal == "Impulse noise") {
                    signal[i] = new ImpulseNoise(A, t1, d, f, p);
                }

                signal[i].generateSignal();
                if (chosenSignal.toString().toLowerCase().contains("impulse")) {
                    generateScatterChart(i);
                } else {
                    generateLineChart(i);
                }
            }
        }
    }

    @FXML
    private void openHistogramWindow(ActionEvent event) {
        Node node = (Node) event.getSource();
        String data = (String) node.getUserData();
        int value = Integer.parseInt(data);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Views/histogram.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Histogram for chart " + (value + 1));
            stage.setScene(new Scene(root1, 640, 640));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HistogramController.signal = signal[value];
    }

    @FXML
    private void openSignalConversionWindow(ActionEvent event) {
        Node node = (Node) event.getSource();
        String data = (String) node.getUserData();
        int value = Integer.parseInt(data);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Views/signalConversion.fxml"));
        try {
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Signal conversion for chart " + (value + 1));
            stage.setScene(new Scene(root1, 900, 900));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SignalConversionController signalConversionController = fxmlLoader.getController();
        signalConversionController.setSignal(signal[value]);
        switch (value) {
            case 0: {
                signalConversionController.setCharts(lineChart1.isVisible() ? lineChart1.getData() : null,
                        scatterChart1.isVisible() ? scatterChart1.getData() : null);
                break;
            }
            case 1: {
                signalConversionController.setCharts(lineChart2.isVisible() ? lineChart2.getData() : null,
                        scatterChart2.isVisible() ? scatterChart2.getData() : null);
                break;
            }
            case 2: {
                signalConversionController.setCharts(lineChart3.isVisible() ? lineChart3.getData() : null,
                        scatterChart3.isVisible() ? scatterChart3.getData() : null);
                break;
            }
            default: {
                Error("Opening window error", "Error while opening signal conversion window");
                break;
            }
        }
    }

    @FXML
    private  void openSensorSimulationWindow(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Views/sensorSimulation.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Sensor simulation");
            stage.setScene(new Scene(root1, 900, 600));
            stage.setResizable(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void calculate() {
        if (firstElement.getValue() == null || secondElement.getValue() == null || result.getValue() == null) {
            Error("Calculation error", "All calculation components must be declared.");
            return;
        }

        int first = getIndex(firstElement.getValue());
        int second = getIndex(secondElement.getValue());
        int res = getIndex(result.getValue());

        if (operationType.getValue().equals("Addition")) {
            signal[res] = signal[first].addition(signal[second]);
        }
        if (operationType.getValue().equals("Subtraction")) {
            signal[res] = signal[first].subtraction(signal[second]);
        }
        if (operationType.getValue().equals("Multiplication")) {
            signal[res] = signal[first].multiplication(signal[second]);
        }
        if (operationType.getValue().equals("Division")) {
            signal[res] = signal[first].division(signal[second]);
        }

        if (chosenSignal.toString().toLowerCase().contains("impulse")) {
            generateScatterChart(res);
        } else {
            generateLineChart(res);
        }
    }

    @FXML
    private void convolute() {
        clearChart3();
        this.signal[2] = new BaseSignal();
        grid3.getChildren().clear();

        this.signal[2].signal = this.signal[0].convolute(this.signal[1], false);
        generateLineChart(2);
        filData(grid3, 2);
    }

    @FXML
    private void correlate() {
        clearChart3();
        this.signal[2] = new BaseSignal();
        grid3.getChildren().clear();

        this.signal[2].signal = this.signal[0].correlate(this.signal[1], correlationType.isSelected());
        generateLineChart(2);
        filData(grid3, 2);
    }

    @FXML
    private void filter() {
        int M = Integer.parseInt(filterMagnitude.getText());
        int K = Integer.parseInt(filterCoefficient.getText());
        String window = windowType.getValue();
        String filter = filterType.getValue();

        this.signal[1] = new BaseSignal();
        this.signal[1].signal = this.signal[0].projectFilter(M, K, window, filter);
        clearChart2();
        grid2.getChildren().clear();
        generateLineChart(1);
        filData(grid2, 1);

        convolute();
    }

    @FXML
    private void clearChart1() {
        lineChart1.getData().clear();
        scatterChart1.getData().clear();
        grid1.getChildren().clear();
    }

    @FXML
    private void clearChart2() {
        lineChart2.getData().clear();
        scatterChart2.getData().clear();
        grid2.getChildren().clear();
    }

    @FXML
    private void clearChart3() {
        lineChart3.getData().clear();
        scatterChart3.getData().clear();
        grid3.getChildren().clear();
    }

    @FXML
    private void saveChart(ActionEvent event) {
        Node node = (Node) event.getSource();
        String data = (String) node.getUserData();
        int value = Integer.parseInt(data);

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Binary files (*.bin)", "*.bin");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(amplitude.getScene().getWindow());

        if (file != null) {
            try {
                if (signal[value] == null) {
                    throw new Exception();
                }
                signal[value].saveToBinary(file);
            } catch (Exception ex) {
                Error("Saving error", "File cannot be saved properly.");
            }
        }
    }

    @FXML
    private void openChart(ActionEvent event) {
        Node node = (Node) event.getSource();
        String data = (String) node.getUserData();
        int value = Integer.parseInt(data);

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Binary files (*.bin)", "*.bin");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(amplitude.getScene().getWindow());

        if (file != null) {
            try {
                if (signal[value] == null) {
                    signal[value] = new BaseSignal();
                }
                signal[value].readFromBinary(file);
            } catch (Exception ex) {
                Error("Opening error", "File cannot be opened properly.");
            }
        }
        if (chosenSignal.toString().toLowerCase().contains("impulse")) {
            generateScatterChart(value);
        } else {
            generateLineChart(value);
        }
    }

    private XYChart.Series<Double, Double> createNewDataSeries(int i) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName(chosenSignal.toString());

        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : signal[i].signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        return series;
    }

    private void generateLineChart(int i) {
        switch (i) {
            case 0: {
                //adding series to the line chart
                clearChart1();
                lineChart1.setVisible(true);
                scatterChart1.setVisible(false);
                lineChart1.getData().add(createNewDataSeries(i));
                lineChart1.setCreateSymbols(false);
                filData(grid1, i);
                break;
            }
            case 1: {
                //adding series to the line chart
                clearChart2();
                lineChart2.setVisible(true);
                scatterChart2.setVisible(false);
                lineChart2.getData().add(createNewDataSeries(i));
                lineChart2.setCreateSymbols(false);
                filData(grid2, i);
                break;
            }
            case 2: {
                //adding series to the line chart
                clearChart3();
                lineChart3.setVisible(true);
                scatterChart3.setVisible(false);
                lineChart3.getData().add(createNewDataSeries(i));
                lineChart3.setCreateSymbols(false);
                filData(grid3, i);
                break;
            }
            default: {
                Error("Generating signal error", "Generating signal error");
                break;
            }
        }
    }

    private void generateScatterChart(int i) {
        switch (i) {
            case 0: {
                clearChart1();
                lineChart1.setVisible(false);
                scatterChart1.setVisible(true);
                scatterChart1.getData().add(createNewDataSeries(i));
                filData(grid1, i);
                break;
            }
            case 1: {
                clearChart2();
                lineChart2.setVisible(false);
                scatterChart2.setVisible(true);
                scatterChart2.getData().add(createNewDataSeries(i));
                filData(grid2, i);
                break;
            }
            case 2: {
                clearChart3();
                lineChart3.setVisible(false);
                scatterChart3.setVisible(true);
                scatterChart3.getData().add(createNewDataSeries(i));
                filData(grid3, i);
                break;
            }
            default: {
                Error("Generating signal error", "Generating signal error");
                break;
            }
        }
    }

    private void filData(GridPane grid, int i) {
        grid.getChildren().clear();
        grid.add(new Label("Average:"), 0, 0);
        grid.add(new Label("Absolute average:"), 0, 1);
        grid.add(new Label("Root mean square:"), 0, 2);
        grid.add(new Label("Variance:"), 0, 3);
        grid.add(new Label("Effective value:"), 0, 4);

        grid.add(new Label(Double.toString(signal[i].average)), 1, 0);
        grid.add(new Label(Double.toString(signal[i].absoluteAverage)), 1, 1);
        grid.add(new Label(Double.toString(signal[i].rms)), 1, 2);
        grid.add(new Label(Double.toString(signal[i].variance)), 1, 3);
        grid.add(new Label(Double.toString(signal[i].effectiveValue)), 1, 4);
    }

    private int getIndex(String name) {
        if (name.equals("Chart 1")) {
            return 0;
        } else if (name.equals("Chart 2")) {
            return 1;
        } else {
            return 2;
        }
    }

    private void Error(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean validateParameter() {
        if ((amplitude.isVisible() && amplitude.getText().isEmpty()) ||
                (startingPoint.isVisible() && startingPoint.getText().isEmpty()) ||
                (period.isVisible() && period.getText().isEmpty()) ||
                (duration.isVisible() && duration.getText().isEmpty()) ||
                (dutyCycle.isVisible() && dutyCycle.getText().isEmpty()) ||
                (step.isVisible() && step.getText().isEmpty()) ||
                (frequency.isVisible() && frequency.getText().isEmpty()) ||
                (sampleNumber.isVisible() && sampleNumber.getText().isEmpty()) ||
                (probability.isVisible() && probability.getText().isEmpty())
        ) {
            Error("Parameter error", "All parameters are required");
            return false;
        }
        return true;
    }

    private void setParametersVisibility(boolean a, boolean t1, boolean d, boolean T, boolean kw, boolean ts, boolean f, boolean p, boolean ns) {
        amplitude.setVisible(a);
        startingPoint.setVisible(t1);
        duration.setVisible(d);
        period.setVisible(T);
        dutyCycle.setVisible(kw);
        step.setVisible(ts);
        frequency.setVisible(f);
        probability.setVisible(p);
        sampleNumber.setVisible(ns);
    }

    private ObservableList<String> signalList = observableArrayList(
            "Uniform noise", "Gaussian noise",
            "Sine wave", "Half-wave rectified sine", "Full-wave rectifier sine",
            "Square wave", "Symmetrical square wave", "Triangle wave",
            "Unit step function", "Kronecker impulse", "Impulse noise");

    private ObservableList<String> operationList = observableArrayList(
            "Addition", "Subtraction",
            "Multiplication", "Division");

    private ObservableList<String> chartList = observableArrayList(
            "Chart 1", "Chart 2",
            "Chart 3");

    private ObservableList<String> filterList = observableArrayList("Low-pass", "High-pass", "Band-pass");
    private ObservableList<String> windowList = observableArrayList(null, "Hamming", "Hanning", "Blackman");
}