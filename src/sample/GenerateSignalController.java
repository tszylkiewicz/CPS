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
    private void chooseSignal(ActionEvent event) {
        chosenSignal = signalType.getValue();
        if (chosenSignal == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Signal generator error");
            alert.setContentText("No signal chosen.");
            alert.showAndWait();
            return;
        } else {
            //A, t1, d
            if (chosenSignal == "Uniform noise" || chosenSignal == "Gaussian noise") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                //A, T, t1, d
            } else if (chosenSignal == "Sine wave" || chosenSignal == "Half-wave rectified sine" || chosenSignal == "Full-wave rectifier sine") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
                period.setVisible(true);
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
                //A, n1, ns, l, f
            } else if (chosenSignal == "Kronecker impulse") {
                //A, t1, d, f, p
            } else if (chosenSignal == "Impulse noise") {
                amplitude.setVisible(true);
                startingPoint.setVisible(true);
                duration.setVisible(true);
            }
        }
    }

    @FXML
    private void generateSignal(ActionEvent event) {
        float A = Float.parseFloat(amplitude.getCharacters().toString());
        float T =Float.parseFloat(period.getCharacters().toString());
        float t1 =Float.parseFloat(startingPoint.getCharacters().toString());
        float d =Float.parseFloat(duration.getCharacters().toString());
        ArrayList<Double> X = formula.generateX(t1, d);
        ArrayList<Double> Y = formula.sin(X, A, T, t1);
        System.out.println(X);
        System.out.println(Y);
    }
}
