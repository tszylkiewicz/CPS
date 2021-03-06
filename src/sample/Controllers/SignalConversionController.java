package sample.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import sample.Signals.BaseSignal;

import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;

public class SignalConversionController implements Initializable {


    public TextField samplingRate;
    public TextField quantizationLevel;
    public TextField reconstructionFreq;

    public double MSE;
    public double SNR;
    public double PSNR;
    public double MD;

    @FXML
    private GridPane measures;
    @FXML
    private LineChart<Double, Double> lineChart;
    @FXML
    private ScatterChart<Double, Double> scatterChart;

    private BaseSignal originalSignal;
    private BaseSignal reconstructedSignal;
    private BaseSignal tempSignal;

    private TreeMap<Double, Double> restoredSignal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UnaryOperator<TextFormatter.Change> filter = t -> {

            if (t.isReplaced())
                if (t.getText().matches("[^0-9]"))
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


            if (t.isAdded()) {
                if (t.getText().matches("[^0-9]")) {
                    t.setText("");
                }
            }

            return t;
        };

        samplingRate.setTextFormatter(new TextFormatter<>(filter));
        reconstructionFreq.setTextFormatter(new TextFormatter<>(filter));
        lineChart.setVisible(true);
        scatterChart.setVisible(false);
        reconstructionFreq.setText("1000");
    }

    public void setSignal(BaseSignal signal) {
        this.originalSignal = new BaseSignal(signal);
        this.restoredSignal = signal.signal;
        this.tempSignal = new BaseSignal(signal);
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

    @FXML
    private void clearChart() {
        lineChart.getData().clear();
        scatterChart.getData().clear();
        measures.getChildren().clear();
        addDataToChart("Original Signal", restoredSignal);
    }

    @FXML
    private void sample() {
        float temp = (float) (1.0 / Float.parseFloat(samplingRate.getText()));
        this.tempSignal.signal = this.originalSignal.sample(temp);
        this.tempSignal.step = temp;
        this.tempSignal.signalFrequency = Float.parseFloat(samplingRate.getText());
        addDataToChart("Sampling result", this.tempSignal.signal);
    }

    @FXML
    private void quantifyCut() {
        this.tempSignal.quantify(Integer.parseInt(quantizationLevel.getText()), 0);
        addDataToChart("Quantization result", this.tempSignal.signal);
    }

    @FXML
    private void quantifyRound() {
        this.tempSignal.quantify(Integer.parseInt(quantizationLevel.getText()), 1);
        addDataToChart("Quantization result", this.tempSignal.signal);
    }

    @FXML
    private void ZOH() {
        this.tempSignal.step = (float) (1.0 / Integer.parseInt(reconstructionFreq.getText()));
        this.tempSignal.signalFrequency = Integer.parseInt(reconstructionFreq.getText());
        this.reconstructedSignal = this.tempSignal.ZOH();
        addDataToChart("Reconstructed signal - ZOH", this.reconstructedSignal.signal);
        filData();
    }

    @FXML
    private void Sinc() {
        double freq = Math.round(this.tempSignal.step * 10000.00) / 10000.00;
        this.tempSignal.step = (float) (1.0 / Integer.parseInt(reconstructionFreq.getText()));
        this.tempSignal.signalFrequency = Integer.parseInt(reconstructionFreq.getText());
        this.reconstructedSignal = this.tempSignal.SincInterpolation(freq);
        addDataToChart("Reconstructed signal - Sinc", this.reconstructedSignal.signal);
        filData();
    }

    private void addDataToChart(String title, TreeMap<Double, Double> data) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName(title);

        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        this.lineChart.getData().add(series);
    }

    private void countMSE() {
        this.MSE = 0;
        for (Double key : this.originalSignal.signal.keySet()) {
            if (this.reconstructedSignal.signal.get(key) != null) {
                this.MSE += Math.pow(this.originalSignal.signal.get(key) - this.reconstructedSignal.signal.get(key), 2);
            }
        }
        this.MSE = Math.round((this.MSE / this.originalSignal.signal.size()) * 10000.00) / 10000.00;
    }

    private void countSNR() {
        this.SNR = 0;
        double numerator = 0;
        double denominator = 0;
        for (Double key : this.originalSignal.signal.keySet()) {
            if (this.reconstructedSignal.signal.get(key) != null) {
                numerator += Math.pow(this.originalSignal.signal.get(key), 2);
                denominator += Math.pow(this.originalSignal.signal.get(key) - this.reconstructedSignal.signal.get(key), 2);
            }
        }
        this.SNR = Math.round((10 * Math.log10(numerator / denominator)) * 10000.00) / 10000.00;
    }

    private void countPSNR() {
        this.PSNR = Math.round((10 * Math.log10(Collections.max(this.originalSignal.signal.values()) / this.MSE)) * 10000.00) / 10000.00;
    }

    private void countMD() {
        List<Double> temp = new ArrayList<>();
        for (Double key : this.originalSignal.signal.keySet()) {
            if (this.reconstructedSignal.signal.get(key) != null) {
                temp.add(Math.abs(this.originalSignal.signal.get(key) - this.reconstructedSignal.signal.get(key)));
            }
        }
        this.MD = Math.round(Collections.max(temp) * 10000.00) / 10000.00;
    }

    private void filData() {
        countMSE();
        countSNR();
        countPSNR();
        countMD();
        measures.getChildren().clear();
        measures.add(new Label("Mean Squared Root:"), 0, 0);
        measures.add(new Label("Signal to Noise Ratio:"), 0, 1);
        measures.add(new Label("Peak Signal to Noise Ratio:"), 0, 2);
        measures.add(new Label("Maximum Difference:"), 0, 3);

        measures.add(new Label(Double.toString(this.MSE)), 1, 0);
        measures.add(new Label(Double.toString(this.SNR)), 1, 1);
        measures.add(new Label(Double.toString(this.PSNR)), 1, 2);
        measures.add(new Label(Double.toString(this.MD)), 1, 3);
    }
}
