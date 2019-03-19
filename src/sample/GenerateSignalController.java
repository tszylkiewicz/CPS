package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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
    public Button generateSignal;
    public TextField amplitude;
    public TextField startingPoint;
    public TextField duration;
    public TextField period;
    public TextField dutyCycle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
                    } else if (t.getText().matches("[^0-9\\.]")) {
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
    }

    @FXML
    private void test(ActionEvent event) {
        Object chosenSignal = signalType.getValue();
        if (chosenSignal == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Signal generator error");
            alert.setContentText("No signal chosen.");
            alert.showAndWait();
            return;
        } else {
            System.out.println(chosenSignal);
            System.out.println(amplitude.getCharacters());
            System.out.println(startingPoint.getCharacters());
            System.out.println(duration.getCharacters());
            System.out.println(period.getCharacters());
            System.out.println(dutyCycle.getCharacters());
        }
    }
}
