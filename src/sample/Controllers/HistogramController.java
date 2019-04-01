package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import sample.Signals.BaseSignal;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class HistogramController implements Initializable {

    //Parts number
    public TextField partsNumber;
    private double min, step;

    //Histogram
    public BarChart<String, Number> histogram;

    @FXML
    private CategoryAxis xAxis ;
    @FXML
    private NumberAxis yAxis ;

    static BaseSignal signal;

    @FXML
    private void generateHistogram() {
        int parts = Integer.parseInt(partsNumber.getText());
        histogram.getData().clear();
        xAxis.setLabel("Part number");
        yAxis.setLabel("Samples number");
        try {
            signal.getDataForHistogram(parts);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        min = signal.minValueHist;
        step = (signal.maxValueHist - signal.minValueHist)/ parts;
        histogram.getData().add(createNewDataSeriesBarChart());
        histogram.autosize();
    }

    private XYChart.Series<String, Number> createNewDataSeriesBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Histogram");

//        Double minValue = signal[i].minValueHist;
//        Double scope = signal[i].scope;
//        Double startPoint = minValue;

        //adding data from signals to series
        for (int j = 0; j < signal.histogramTableValue.length; j++) {
            series.getData().add(new XYChart.Data(Math.round((min + (j*step)) * 100.00) / 100.00+ " to " + Math.round((min + ((j+1)*step)) * 100.00) / 100.00 , signal.histogramTableValue[j]));
        }

        return series;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        histogram.setVisible(true);
        histogram.setAnimated(false);

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

        partsNumber.setTextFormatter(new TextFormatter<>(filter));
    }
}
