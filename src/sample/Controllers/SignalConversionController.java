package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import sample.Signals.BaseSignal;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class SignalConversionController implements Initializable {


    public TextField samplingRate;

    private HashMap<Double, Double> samples;
    private  BaseSignal originalSignal;
    private  BaseSignal reconstructedSignal;
    private LineChart<Double, Double> lineChart;
    private ScatterChart<Double, Double> scatterChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UnaryOperator<TextFormatter.Change> filter = t -> {

            if (t.isReplaced())
                if (t.getText().matches("[^0-9]"))
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


            if (t.isAdded()) {
                if (t.getText().matches("[^0-9.]")) {
                    t.setText("");
                }
            }

            return t;
        };

        samplingRate.setTextFormatter(new TextFormatter<>(filter));
    }

    @FXML
    private void sample() {
        this.samples = this.originalSignal.sample(Float.parseFloat(samplingRate.getText()));
    }

    @FXML
    private void quantify(){

    }

    public void setSignal(BaseSignal signal) {
        this.originalSignal = signal;
    }

    public void setCharts(LineChart lineChart, ScatterChart scatterChart){
        this.lineChart = lineChart;
        this.scatterChart = scatterChart;
    }
}
