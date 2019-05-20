package sample.Controllers;

import javafx.beans.binding.DoubleExpression;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import sample.Signals.BaseSignal;
import sample.Signals.SineWave;
import sun.misc.Signal;
import sun.reflect.generics.tree.Tree;

import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;

public class SensorController implements Initializable {
    //Parameters
    public TextField simulationBaseTime;        //100
    public TextField objectVelocity;            //10
    public TextField waveVelocity;              //10000
    public TextField simulationSignalPeriod;    //1
    public TextField simulationSignalFrequency; //100
    public TextField bufferLength;              //200
    public TextField simulationTestPeriod;      //10


    public LineChart<Double, Double> lineChart1;
    public LineChart<Double, Double> lineChart2;
    public LineChart<Double, Double> lineChart3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UnaryOperator<TextFormatter.Change> filter = t -> {

            if (t.isReplaced())
                if (t.getText().matches("[^0-9]"))
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));


            if (t.isAdded()) {
                if (t.getControlText().contains("-")) {
                    if (t.getText().matches("[^0-9.]")) {
                        t.setText("");
                    }
                }
                if (t.getControlText().contains(".")) {
                    if (t.getText().matches("[^0-9]")) {
                        t.setText("");
                    }
                } else if (t.getText().matches("[^0-9.-]")) {
                    t.setText("");
                }
            }

            return t;
        };

        simulationBaseTime.setTextFormatter(new TextFormatter<>(filter));
        objectVelocity.setTextFormatter(new TextFormatter<>(filter));
        waveVelocity.setTextFormatter(new TextFormatter<>(filter));
        simulationSignalPeriod.setTextFormatter(new TextFormatter<>(filter));
        simulationSignalFrequency.setTextFormatter(new TextFormatter<>(filter));
        bufferLength.setTextFormatter(new TextFormatter<>(filter));
        simulationTestPeriod.setTextFormatter(new TextFormatter<>(filter));

        simulationBaseTime.setText("100");
        objectVelocity.setText("10");
        waveVelocity.setText("10000");
        simulationSignalPeriod.setText("1");
        simulationSignalFrequency.setText("100");
        bufferLength.setText("200");
        simulationTestPeriod.setText("10");
    }

    @FXML
    private void simulate() {
/*        float sbt = Float.parseFloat(simulationBaseTime.getText());
        float ov = Float.parseFloat(objectVelocity.getText());
        float wv = Float.parseFloat(waveVelocity.getText());
        float ssp = Float.parseFloat(simulationSignalPeriod.getText());
        float ssf = Float.parseFloat(simulationSignalFrequency.getText());
        int bl = Integer.parseInt(bufferLength.getText());
        float stp = Float.parseFloat(simulationTestPeriod.getText());

        SineWave sent = new SineWave(1f, 0f, bl / ssf, ssp, 0);
        SineWave received = null;
        TreeMap<Double, Double> output = new TreeMap<>();

        sent.signalFrequency = ssf;
        sent.step = (float) (1.0 / ssf);
        sent.generateSignal();
        lineChart1.getData().add(createNewDataSeries(sent.signal));

        //double[] usableValues;
        List<Double> usableValues;// = Arrays.asList(new Double[10]);

        double currentTime = 0;
        double objectPosition = 100;
        double relativeVelocity = wv - ov;
        double timeToImpact = 0;
        double waveSendTime = 0;
        double wavePropagationTime = 0;
        double waveComebackTime = 0;
        double calculatedDistance;
        int maximumCollerationIndex;
        double collerationBasedTimeDifference;
        while (currentTime < sbt) {
            //Time before sending the wave
            currentTime += stp;
            objectPosition += stp * ov;

            //Time between sending the wave and reaching the object
            waveSendTime = currentTime;
            timeToImpact = objectPosition / relativeVelocity;
            currentTime += timeToImpact;
            objectPosition += timeToImpact * ov;

            //Time between reaching the object and getting back to the sensor
            waveComebackTime = objectPosition / wv;
            currentTime += waveComebackTime;
            objectPosition += waveComebackTime * ov;

            wavePropagationTime = waveSendTime - currentTime;

            //Comparing the signals
            received = new SineWave(1, (float) wavePropagationTime, bl / ssf, ssp, 0);
            received.signalFrequency = ssf;
            received.step = (float) (1.0 / ssf);
            received.generateSignal();
            //received = Signal.GenerateSensorSignal(bufferLength, simulationSignalFrequency, simulationSignalPeriod, wavePropagationTime);
            output = sent.correlate(received, false);//Signal.Correlation(sent, received);


            //Calculating the distance
            //usableValues = new double[output.size() / 2];
            usableValues = Arrays.asList(new Double[output.size() / 2]);
            for (int i = 0; i < output.size() / 2; i++) {
                usableValues.set(i, 0.0);
            }
            //usableValues = Arrays.asList(new Double[output.size() / 2]);
            for (int i = 0; i < usableValues.size(); i++) {
                usableValues.set(i, output.get((output.size() / 2.0) + i));
            }
            maximumCollerationIndex = 0;

            for (int i = 1; i < usableValues.size() && maximumCollerationIndex == 0; i++) {
                System.out.println(Collections.max(usableValues));
                if (usableValues.get(i).equals(Collections.max(usableValues))) {
                    maximumCollerationIndex = i;
                }
            }


            //signalB = received;

            collerationBasedTimeDifference = (maximumCollerationIndex / ssf);

            calculatedDistance = (collerationBasedTimeDifference * wv);
            System.out.println(output.size());
            System.out.println(usableValues.size());
            //correlationOutput.values = usableValues;
            //correlationOutput.samples = usableValues.size();
            //output = correlationOutput;

            if (collerationBasedTimeDifference == 0) {
                System.out.println("Zbyt niska częstotliwość próbkowania sygnału");
            } else {
                //System.out.println("t = {3}: Odległość rzeczywista: {0}; odległość wyliczona: {1}; Błąd: {2}", objectPosition, calculatedDistance, objectPosition - calculatedDistance, currentTime);
            }
        }

        //lineChart1.getData().add(createNewDataSeries(sent.signal));
        //lineChart2.getData().add(createNewDataSeries(received.signal));
       // lineChart3.getData().add(createNewDataSeries(output));
 */   }

    private XYChart.Series<Double, Double> createNewDataSeries(TreeMap<Double, Double> signal) {
        XYChart.Series<Double, Double> series = new XYChart.Series<>();
        //series.setName(chosenSignal.toString());

        //adding data from signals to series
        for (Map.Entry<Double, Double> entry : signal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        return series;
    }
}
