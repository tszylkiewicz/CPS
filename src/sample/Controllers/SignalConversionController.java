package sample.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import sample.Signals.BaseSignal;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class SignalConversionController implements Initializable {


    public TextField samplingRate;
    public TextField quantizationLevel;

    private HashMap<Double, Double> samples;
    private HashMap<Double, Double> quantizationResult;
    private BaseSignal originalSignal;
    private BaseSignal reconstructedSignal;
    @FXML
    private LineChart<Double, Double> lineChart;
    @FXML
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
        lineChart.setVisible(true);
        scatterChart.setVisible(false);
        //lineChart.setCreateSymbols(false);
    }

    @FXML
    private void sample() {
        this.samples = this.originalSignal.sample(Float.parseFloat(samplingRate.getText()));
        System.out.println(this.samples);
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Chosen samples");

        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : this.samples.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        this.lineChart.getData().add(series);
    }

    @FXML
    private void quantify() {
        this.quantizationResult = this.originalSignal.quantify(Integer.parseInt(quantizationLevel.getText()));
        System.out.println(this.quantizationResult);
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Quantization result");

        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : this.quantizationResult.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        this.lineChart.getData().add(series);
    }

    public void setSignal(BaseSignal signal) {
        this.originalSignal = signal;
    }

    public void setCharts(ObservableList<XYChart.Series<Double, Double>> lineChart, ObservableList<XYChart.Series<Double, Double>> scatterChart) {
        if (lineChart != null) {
            this.lineChart.setVisible(true);
            this.scatterChart.setVisible(false);
            this.lineChart.getData().addAll(lineChart);
        } else {
            this.lineChart.setVisible(false);
            this.scatterChart.setVisible(true);
            this.scatterChart.getData().addAll(scatterChart);
        }


    }
}
