package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class ChooseSignalController {

    ObservableList<String> signalList = FXCollections.observableArrayList("Sygał sinusoidalny","Sygał trójkątny" ,"Sygał prostokątny");

    private ChoiceBox signalType;

    @FXML
    private void initialize() {
        signalType.setValue("mm");
    }
}
