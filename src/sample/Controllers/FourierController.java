package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.complex.Complex;
import sample.Fourier.DiscreteFourierTranform;
import sample.Fourier.UtilsComplex;
import sample.Signals.BaseSignal;
import sample.Signals.CustomSignal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FourierController {


    public LineChart<Double, Double> fourier1;

    public LineChart<Double, Double> fourier2;

    private BaseSignal bs;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private List<Double> listImaginary = new ArrayList<>();

    private List<Double> listReal = new ArrayList<>();


    @FXML
    public void getCustomSignal() {

        CustomSignal cs = new CustomSignal();
        try {
            bs = cs.getCustomSignal(4);
        } catch (Exception e) {
            e.printStackTrace();

        }

        onTransform();
    }


    @FXML
    public void onTransform() {
        generateTransformFourier();

        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");

        fourier1.getData().clear();
        fourier1.setVisible(true);
        fourier1.getData().add(generateRealSeries());

        //rysowanie wykresu oryginalnego S1, S2, S3.
//        fourier1.getData().add(generateSeries_S1_S2_S3_FunctionSeries());


        fourier2.getData().clear();
        fourier2.setVisible(true);
        fourier2.getData().add(generateImaginarySeries());

    }

    private void generateTransformFourier() {
        List<Complex> complexes = UtilsComplex.ConvertRealToComplex((new ArrayList<>(bs.signal.values())));

        try {
            List<Complex> transformed = DiscreteFourierTranform.Transform(complexes);

            if (transformed.isEmpty()) {
                throw new Exception("The transformed parameters is empty.");
            }

            for (Complex cpx : transformed
            ) {
                listImaginary.add(cpx.getImaginary());
                listReal.add(cpx.getReal());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private XYChart.Series<Double, Double> generateImaginarySeries() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Imaginary");

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), listImaginary.get(count)));
            count++;
        }

        return series;
    }


    private XYChart.Series<Double, Double> generateRealSeries() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Real");

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), listReal.get(count)));
            count++;
        }

        return series;
    }

    private XYChart.Series<Double, Double> generateSeries_S1_S2_S3_FunctionSeries() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("S1, S2, S3 functions.");

        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        return series;
    }


}
