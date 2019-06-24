package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import org.apache.commons.math3.complex.Complex;
import sample.Fourier.DiscreteFourierTranform;
import sample.Fourier.UtilsComplex;
import sample.Signals.BaseSignal;
import sample.Signals.CustomSignal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class FourierController implements Initializable {


    public LineChart<Double, Double> fourier1;

    public LineChart<Double, Double> fourier2;

    public NumberAxis xAxisFourier2;

    public NumberAxis yAxisFourier2;

    public ChoiceBox choiceBoxTransformationType;

    public LineChart reverseFourier;

    private BaseSignal bs;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    List<Complex> transformedSignal = new ArrayList<>();

    private List<Double> listImaginary = new ArrayList<>();

    private List<Double> listReal = new ArrayList<>();

    private List<Double> listMagnitude = new ArrayList<>();

    private List<Double> listPhase = new ArrayList<>();

    private List<Double> reverseDFT= new ArrayList();


    @FXML
    public ChoiceBox<String> choiceBoxTypeOfChart = new ChoiceBox<>();

    private static final String W1_REAL_IMAGINARY = "W1 - Real + Imaginary";

    private static final String W2_MAGNITUDE_FAZE = "W2 - Magnitude + Faze";

    private static final String TRANSFORM_DFT = "Discrete Transform Type";

    private static final String TRANSFORM_REVERSE_DFT = "Reverse Discrete Transform Type";


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

        //set default
        choiceBoxTransformationType.setValue(TRANSFORM_DFT);

    }

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

        clearTransformationData();

        if(choiceBoxTransformationType.getValue().equals(TRANSFORM_DFT)) {
            generateDFT();
        } else if (choiceBoxTransformationType.getValue().equals(TRANSFORM_REVERSE_DFT)){
            generateReverseDFT();
        }

        if(choiceBoxTransformationType.getValue().equals(TRANSFORM_REVERSE_DFT)){
            drawReverse();
        } else {
            if(choiceBoxTypeOfChart.getValue().equals(W1_REAL_IMAGINARY) ){
                drawW1();
            } else {
                drawW2();
            }
        }



        //rysowanie CUSTOM -> do przeniesienia do normalnych sygnałów.
//        fourier1.getData().add(generateSeries_S1_S2_S3_FunctionSeries());
    }



    private void clearTransformationData(){
        listMagnitude.clear();
        listPhase.clear();
        listReal.clear();
        listImaginary.clear();
    }

    private void setDiagramsFourier(){
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

    private void drawW1(){
        setDiagramsFourier();
        fourier1.setTitle("Real");
        fourier1.getData().add(getRealSeries());
        fourier2.setTitle("Imaginary");
        fourier2.getData().add(getImaginarySeries());
    }

    private void drawW2(){
        setDiagramsFourier();
        fourier1.setTitle("Magnitude");
        fourier1.getData().add(getMagnitudeSeries());
        fourier2.setTitle("Phase");
        fourier2.getData().add(getPhaseSeries());

    }

    private void drawReverse() {
        fourier1.setVisible(false);
        fourier2.setVisible(false);
        reverseFourier.getData().clear();
        reverseFourier.setTitle("Reverse DFT Signal");
        reverseFourier.setVisible(true);
        reverseFourier.getData().add(generateReverseDFTSeries());
    }




    private void generateDFT() {
        List<Complex> complexes = UtilsComplex.ConvertRealToComplex((new ArrayList<>(bs.signal.values())));

        try {
            transformedSignal = DiscreteFourierTranform.Transform(complexes);

            if (transformedSignal.isEmpty()) {
                throw new Exception("The transformed parameters is empty.");
            }

            for (Complex cpx : transformedSignal
            ) {
                listImaginary.add(cpx.getImaginary());
                listReal.add(cpx.getReal());
                listMagnitude.add(cpx.abs());
                listPhase.add(cpx.getArgument());
                //The result from phase is between -Pi to Pi.
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<Double> generateReverseDFT() {
        try {
            return DiscreteFourierTranform.TransformBack(transformedSignal);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private XYChart.Series<Double, Double> generateReverseDFTSeries() {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Reverse DFT");
        List<Double> reverseDFT = generateReverseDFT();

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), reverseDFT.get(count)));
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
            series.getData().add(new XYChart.Data<>(entry.getKey(), listImaginary.get(count)));
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
            series.getData().add(new XYChart.Data<>(entry.getKey(), listReal.get(count)));
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
            series.getData().add(new XYChart.Data<>(entry.getKey(), listPhase.get(count)));
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
            series.getData().add(new XYChart.Data<>(entry.getKey(), listMagnitude.get(count)));
            count++;
        }

        return series;
    }

    private XYChart.Series<Double, Double> getReverseDFTSeries(List<Double> signal) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        series.setName("Reverse DFT.");

        int count = 0;
        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : bs.signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), signal.get(count)));
            count++;
        }

        return series;
    }

    //rysuje customowy wykres
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
