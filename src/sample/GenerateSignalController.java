package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import static javafx.collections.FXCollections.observableArrayList;

public class GenerateSignalController implements Initializable {

    ObservableList<String> signalList = observableArrayList(
            "Uniform noise", "Gaussian noise",
            "Sine wave", "Half-wave rectified sine", "Full-wave rectifier sine",
            "Square wave", "Symmetrical square wave", "Triangle wave",
            "Unit step function", "Kronecker impulse", "Impulse noise");

    @FXML
    private ChoiceBox signalType;

    public Button chooseSignal;
    public Button generateSignal;

    public TextField amplitude; //A
    public TextField startingPoint; //t1
    public TextField duration;  //d
    public TextField period;    //T
    public TextField dutyCycle; //kw
    public TextField step; //ts
    public TextField frequency; //f
    public TextField probability; //p
    public TextField sampleNumber; //ns

    private Object chosenSignal;
    private Formula formula;

    private BaseSignal signal;

    @FXML
    private LineChart<Double, Double> lineChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formula = new Formula();
        signalType.setItems(signalList);
        UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {

            @Override
            public TextFormatter.Change apply(TextFormatter.Change t) {

                if (t.isReplaced())
                    if (t.getText().matches("[^0-9]"))
                        t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


                if (t.isAdded()) {
                    if (t.getControlText().contains(".")) {
                        if (t.getText().matches("[^0-9]")) {
                            t.setText("");
                        }
                    } else if (t.getText().matches("[^0-9.]")) {
                        t.setText("");
                    }
                }

                return t;
            }
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

        amplitude.setVisible(false);
        startingPoint.setVisible(false);
        duration.setVisible(false);
        period.setVisible(false);
        dutyCycle.setVisible(false);
        step.setVisible(false);
        frequency.setVisible(false);
        probability.setVisible(false);
        sampleNumber.setVisible(false);
    }

    @FXML
    private void chooseSignal() {
        chosenSignal = signalType.getValue();
        if (chosenSignal == null) {
            Error("Error", "Signal generator error", "No signal chosen.");
        } else {
            //A, t1, d
            if (chosenSignal == "Uniform noise" || chosenSignal == "Gaussian noise") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(false);
                dutyCycle.setVisible(false);
                step.setVisible(false);
                frequency.setVisible(false);
                probability.setVisible(false);
                sampleNumber.setVisible(false);
                //A, T, t1, d
            } else if (chosenSignal == "Sine wave" || chosenSignal == "Half-wave rectified sine" || chosenSignal == "Full-wave rectifier sine") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(true);
                dutyCycle.setVisible(false);
                step.setVisible(false);
                frequency.setVisible(false);
                probability.setVisible(false);
                sampleNumber.setVisible(false);
                //A, T, t1, d, kw
            } else if (chosenSignal == "Square wave" || chosenSignal == "Symmetrical  square wave" || chosenSignal == "Triangle wave") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(true);
                dutyCycle.setVisible(true);
                step.setVisible(false);
                frequency.setVisible(false);
                probability.setVisible(false);
                sampleNumber.setVisible(false);
                //A, t1, d, ts
            } else if (chosenSignal == "Unit step function") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(false);
                dutyCycle.setVisible(false);
                step.setVisible(true);
                frequency.setVisible(false);
                probability.setVisible(false);
                sampleNumber.setVisible(false);
                //A, t1, d, f, ns
            } else if (chosenSignal == "Kronecker impulse") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(false);
                dutyCycle.setVisible(false);
                step.setVisible(false);
                frequency.setVisible(true);
                probability.setVisible(false);
                sampleNumber.setVisible(true);
                //A, t1, d, f, p
            } else if (chosenSignal == "Impulse noise") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(false);
                dutyCycle.setVisible(false);
                step.setVisible(false);
                frequency.setVisible(true);
                probability.setVisible(true);
                sampleNumber.setVisible(false);
            }
        }
    }

    @FXML
    private void generateSignal() {
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


            if (chosenSignal == "Uniform noise") {
                signal = new UniformNoise(A, t1, d, 0);
            }
            if (chosenSignal == "Gaussian noise") {
                //Y = formula.gaussianNoise(X, A);
            }
            if (chosenSignal == "Sine wave") {
                signal = new SineWave(A, t1, d, T, 0);
            }
            if (chosenSignal == "Half-wave rectified sine") {
                signal = new SineWave(A, t1, d, T, 1);
            }
            if (chosenSignal == "Full-wave rectifier sine") {
                signal = new SineWave(A, t1, d, T, 2);
            }
            if (chosenSignal == "Square wave") {
                signal = new SquareWave(A, t1, d, T, kw, 0);
            }
            if (chosenSignal == "Symmetrical square wave") {
                signal = new SquareWave(A, t1, d, T, kw, 1);
            }
            if (chosenSignal == "Triangle wave") {
                signal = new SquareWave(A, t1, d, T, kw, 2);
            }
            if (chosenSignal == "Unit step function") {
                signal = new UnitStep(A, t1, d, ts);
            }
            if (chosenSignal == "Kronecker impulse") {
                signal = new KroneckerImpulse(A, t1, d, f, ns);
            }
            if (chosenSignal == "Impulse noise") {
                signal = new ImpulseNoise(A, t1, d, f, p);
            }

            signal.generateSignal();
            //System.out.println(signal.A);
            //System.out.println(signal.t1);
            //System.out.println(signal.d);
            System.out.println(signal.signal.keySet());
            System.out.println(signal.signal.values());
            //signal.addition(signal);
            //signal.subtraction(signal);
            //signal.multiplication(signal);
            //signal.division(signal);
        }
    }

    public void generateLineChart() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName(chosenSignal.toString());

        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : signal.signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        //adding series to the line chart
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
    }

    @FXML
    public void clearLineChart(){
        lineChart.getData().clear();
    }

    private void Error(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
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
            Error("Error", "Parameter error", "All parameters are required");
            return false;
        }
        return true;
    }
}
