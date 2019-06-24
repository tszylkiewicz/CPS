package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.math3.complex.Complex;
import sample.Fourier.DiscreteFourierTranform;
import sample.Fourier.FastFourierTransfrom;
import sample.Fourier.UtilsComplex;
import sample.Fourier.WaveletTransform;
import sample.Signals.BaseSignal;
import sample.Signals.CustomSignal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class FourierController implements Initializable {


    private static final String W1_REAL_IMAGINARY = "W1 - Real + Imaginary";

    private static final String W2_MAGNITUDE_FAZE = "W2 - Magnitude + Faze";

    private static final String TRANSFORM_DFT = "Discrete Transform Type";

    private static final String TRANSFORM_REVERSE_DFT = "Reverse Discrete Transform Type";

    private static final String TRANSFORM_FFT = "Fast Transform Fourier";

    private static final String TRANSFORM_REVERSE_FFT = "Reverse Fast Fourier Transform";

    private static final String WAVELET_TRANSFORM = "Wavelet Transformation";


    //top chart
    public LineChart<Double, Double> fourier1;

    //bottom chart
    public LineChart<Double, Double> fourier2;

    public NumberAxis xAxisFourier2;

    public NumberAxis yAxisFourier2;

    public ChoiceBox choiceBoxTransformationType;

    public LineChart reverseFourier;

    @FXML
    public ChoiceBox<String> choiceBoxTypeOfChart = new ChoiceBox<>();

    //duration custom signal
    public TextField duration;

    //time of transformation
    public Label transformationTime;

    List<Complex> transformedSignal = new ArrayList<>();

    private BaseSignal bs;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private List<Double> listImaginary = new ArrayList<>();

    private List<Double> listReal = new ArrayList<>();

    private List<Double> listMagnitude = new ArrayList<>();

    private List<Double> listPhase = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fourier1.setVisible(true);
        fourier2.setVisible(true);
        reverseFourier.setVisible(false);
        choiceBoxTypeOfChart.getItems().add(W1_REAL_IMAGINARY);
        choiceBoxTypeOfChart.getItems().add(W2_MAGNITUDE_FAZE);

        //set default
        choiceBoxTypeOfChart.setValue(W1_REAL_IMAGINARY);

        choiceBoxTransformationType.getItems().add(TRANSFORM_DFT);
        choiceBoxTransformationType.getItems().add(TRANSFORM_REVERSE_DFT);
        choiceBoxTransformationType.getItems().add(TRANSFORM_FFT);
        choiceBoxTransformationType.getItems().add(TRANSFORM_REVERSE_FFT);
        choiceBoxTransformationType.getItems().add(WAVELET_TRANSFORM);

        //set default
        choiceBoxTransformationType.setValue(TRANSFORM_DFT);

    }

    @FXML
    public void getCustomSignal() {

        CustomSignal cs = new CustomSignal();
        try {
            bs = cs.getCustomSignal(Integer.parseInt(duration.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        drawCustomSignal("Custom signal S1+S2+S3.");

    }


    @FXML
    public void onTransform() {

        clearTransformationData();

        if (choiceBoxTransformationType.getValue().equals(TRANSFORM_DFT)) {
            generateDFT();
        } else if (choiceBoxTransformationType.getValue().equals(TRANSFORM_REVERSE_DFT)) {
            generateReverseDFT();
        } else if (choiceBoxTransformationType.getValue().equals(TRANSFORM_FFT)) {
            generateFFT();
        } else if (choiceBoxTransformationType.getValue().equals(TRANSFORM_REVERSE_FFT)) {
            generateReverseFFT();
        } else if (choiceBoxTransformationType.getValue().equals(WAVELET_TRANSFORM)){
            genereteWavelet();
        }

        if (choiceBoxTransformationType.getValue().equals(TRANSFORM_REVERSE_DFT)) {
            drawReverseDFT("Reverse DFT Signal");
        } else if (choiceBoxTransformationType.getValue().equals(TRANSFORM_REVERSE_FFT)){
            drawReverseFFT("Reverse FFT Signal");
        }
            else {
            if (choiceBoxTypeOfChart.getValue().equals(W1_REAL_IMAGINARY)) {
                drawW1();
            } else {
                drawW2();
            }
        }
    }


    private void clearTransformationData() {
        listMagnitude.clear();
        listPhase.clear();
        listReal.clear();
        listImaginary.clear();
    }

    private void setDiagramsFourier() {
        fourier1.getData().clear();
        fourier2.getData().clear();

        fourier1.setVisible(true);
        fourier2.setVisible(true);
        reverseFourier.setVisible(false);


        xAxis.setLabel("X Axis");
        yAxis.setLabel("Y Axis");

        xAxisFourier2.setLabel("X Axis");
        yAxisFourier2.setLabel("Y Axis");

        fourier1.getData().clear();
        fourier1.setVisible(true);

        fourier2.getData().clear();
        fourier2.setVisible(true);
    }

    private void drawW1() {
        setDiagramsFourier();
        fourier1.setTitle("Real");
        fourier1.getData().add(getRealSeries());
        fourier2.setTitle("Imaginary");
        fourier2.getData().add(getImaginarySeries());
    }

    private void drawW2() {
        setDiagramsFourier();
        fourier1.setTitle("Magnitude");
        fourier1.getData().add(getMagnitudeSeries());
        fourier2.setTitle("Phase");
        fourier2.getData().add(getPhaseSeries());

    }


    private void setDiagramReverse(){
        fourier1.setVisible(false);
        fourier2.setVisible(false);
        reverseFourier.getData().clear();
        reverseFourier.setVisible(true);

    }

    private void drawReverseDFT(String title) {
        setDiagramReverse();
        reverseFourier.setTitle(title);

        reverseFourier.getData().add(generateReverseDFT());
    }

    private void drawReverseFFT(String title) {
        setDiagramReverse();
        reverseFourier.setTitle(title);
        reverseFourier.getData().add(generateReverseFFT());
    }

    private void drawCustomSignal(String title) {
        setDiagramReverse();
        reverseFourier.setTitle(title);
        reverseFourier.getData().add(generateCustomSignal());
    }




    private void genereteWavelet() {
        try {
            transformedSignal.clear();
            long startTime = System.currentTimeMillis();

            List<Double> points = new ArrayList<>();

            for (Double val: bs.signal.values()
                 ) {
                points.add(val);
            }

                    transformedSignal = WaveletTransform.WaveletTransformation(points);
            long stopTime = System.currentTimeMillis();
            setDurationTime(startTime, stopTime);

            if (transformedSignal.isEmpty()) {
                throw new Exception("The transformed parameters is empty.");
            }

            fillComplexList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void generateDFT() {
        List<Complex> complexes = UtilsComplex.ConvertRealToComplex((new ArrayList<>(bs.signal.values())));

        try {
            transformedSignal.clear();
            long startTime = System.currentTimeMillis();
            transformedSignal = DiscreteFourierTranform.Transform(complexes);
            long stopTime = System.currentTimeMillis();
            setDurationTime(startTime, stopTime);

            if (transformedSignal.isEmpty()) {
                throw new Exception("The transformed parameters is empty.");
            }

            fillComplexList();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private XYChart.Series<Double, Double> generateReverseDFT() {
        try {
            List<Double> listOfReverse = DiscreteFourierTranform.TransformBack(transformedSignal);
            return generateReverseSeries("Reverse of DFT", listOfReverse);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void generateFFT() {
        List<Complex> complexes = UtilsComplex.ConvertRealToComplex((new ArrayList<>(bs.signal.values())));

        transformedSignal.clear();

        long startTime = System.currentTimeMillis();
        transformedSignal = FastFourierTransfrom.FastFourierTransform(complexes);
        long stopTime = System.currentTimeMillis();
        setDurationTime(startTime, stopTime);

        if (transformedSignal.isEmpty()) {
            try {
                throw new Exception("The transformed parameters is empty.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fillComplexList();

    }

    private void setDurationTime(long startTime, long stopTime) {
        long duration = (stopTime - startTime); //ms
        transformationTime.setText(duration + " ms");

    }

    private XYChart.Series<Double, Double> generateReverseFFT() {
        List<Double> listOfReverse = FastFourierTransfrom.FastFourierBackwardTransformation(transformedSignal);
        return generateReverseSeries("Reverse of FFT", listOfReverse);

    }

    private XYChart.Series<Double, Double> generateCustomSignal() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Custom signal");

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            count++;
        }

        return series;

    }


    private void fillComplexList() {
        for (Complex cpx : transformedSignal
        ) {
            listImaginary.add(cpx.getImaginary());
            listReal.add(cpx.getReal());
            listMagnitude.add(cpx.abs());
            listPhase.add(cpx.getArgument());
            //The result from phase is between -Pi to Pi.
        }
    }


    private XYChart.Series<Double, Double> generateReverseSeries(String title, List<Double> reverse) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName(title);

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), reverse.get(count)));
            count++;
        }

        return series;

    }

    private XYChart.Series<Double, Double> getImaginarySeries() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Imaginary");

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            if(listImaginary.size()-1 < count) {
                break;
            } else {
                series.getData().add(new XYChart.Data<>(entry.getKey(), listImaginary.get(count)));
            }
            count++;
        }

        return series;
    }


    private XYChart.Series<Double, Double> getRealSeries() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Real");

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            if (listReal.size()-1 < count) {
                break;
            } else {
                series.getData().add(new XYChart.Data<>(entry.getKey(), listReal.get(count)));
            }
            count++;
        }

        return series;
    }


    private XYChart.Series<Double, Double> getPhaseSeries() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Phase");

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            if (listPhase.size()-1 < count) {
                break;
            } else {
                series.getData().add(new XYChart.Data<>(entry.getKey(), listPhase.get(count)));
            }
            count++;
        }

        return series;
    }


    private XYChart.Series<Double, Double> getMagnitudeSeries() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Magnitude");

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            if(listMagnitude.size()-1 < count) {
                break;
            } else {
                series.getData().add(new XYChart.Data<>(entry.getKey(), listMagnitude.get(count)));
            }
            count++;
        }

        return series;
    }


}
