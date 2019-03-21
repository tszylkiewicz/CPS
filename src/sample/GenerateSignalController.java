package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class GenerateSignalController implements Initializable {

    ObservableList<String> signalList = FXCollections.observableArrayList(
            "Uniform noise", "Gaussian noise",
            "Sine wave", "Half-wave rectified sine", "Full-wave rectifier sine",
            "Square wave", "Symmetrical  square wave", "Triangle wave",
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

    private Object chosenSignal;
    private int signalGroup;
    private Formula formula;

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

        amplitude.setVisible(false);
        startingPoint.setVisible(false);
        duration.setVisible(false);
        period.setVisible(false);
        dutyCycle.setVisible(false);
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
                //A, T, t1, d
            } else if (chosenSignal == "Sine wave" || chosenSignal == "Half-wave rectified sine" || chosenSignal == "Full-wave rectifier sine") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(true);
                dutyCycle.setVisible(false);
                //A, T, t1, d, kw
            } else if (chosenSignal == "Square wave" || chosenSignal == "Symmetrical  square wave" || chosenSignal == "Triangle wave") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(true);
                dutyCycle.setVisible(true);
                //A, t1, d, ts
            } else if (chosenSignal == "Unit step function") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(false);
                dutyCycle.setVisible(false);
                //A, n1, ns, l, f
            } else if (chosenSignal == "Kronecker impulse") {
                //A, t1, d, f, p
            } else if (chosenSignal == "Impulse noise") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(false);
                dutyCycle.setVisible(false);
            }
        }
    }

    @FXML
    private void generateSignal() {
        if (validateParameter()) {
            float A = 0, T = 0, t1 = 0, d = 0, kw = 0;
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
            ArrayList<Double> X = formula.generateX(t1, d);
            ArrayList<Double> Y = new ArrayList<>();
            if (chosenSignal == "Sine wave") {
                Y = formula.sin(X, A, T, t1, 0);
            }
            if (chosenSignal == "Half-wave rectified sine") {
                Y = formula.sin(X, A, T, t1, 1); //half
            }
            if (chosenSignal == "Full-wave rectifier sine") {
                Y = formula.sin(X, A, T, t1, 2); //full
            }
            if(chosenSignal == "Uniform noise") {
                Y = formula.uniformNoise(X, A);
            }
            if(chosenSignal == "Gaussian noise") {
                Y = formula.gaussianNoise(X, A);
            }

            System.out.println(X);
            System.out.println(Y);
        }
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
                (dutyCycle.isVisible() && dutyCycle.getText().isEmpty())) {
            Error("Error", "Parameter error", "All parameters are required");
            return false;
        }
        return true;
    }
}
