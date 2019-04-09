package sample.Signals;

import java.util.HashMap;
import java.io.*;

public class BaseSignal {

    float step = 0.01f;
    private boolean complex = false;

    //Parameters
    float A;
    float t1;
    float d;
    public HashMap<Double, Double> signal;
    public double average;
    public double absoluteAverage;
    public double rms;
    public double variance;
    public double effectiveValue;

    //histogram parameters
    public int[] histogramTableValue;
    public double minValueHist;
    public double maxValueHist;

    public BaseSignal() { }

    BaseSignal(float A, float t1, float d) {
        this.A = A;
        this.t1 = t1;
        this.d = d;
        signal = new HashMap<>();
    }

    public void generateSignal() {
        float t2 = t1 + d;
        double x, y;
        for (double t = t1; Math.round(t * 100.00) / 100.00 <= t2; t += step) {
            x = Math.round(t * 100.00) / 100.00;
            y = Math.round(signalFunction(x) * 100000.00) / 100000.00;
            signal.put(x, y);
        }
        countAverage();
        countAbsoluteAverage();
        countRms();
        countVariance();
        countEffectiveValue();
    }

    public double signalFunction(double x) {
        return x;
    }

    private void countAverage() {
        for (Double y : signal.values()
        ) {
            average += y;
        }
        average = average / (double) signal.size();
        average = Math.round(average * 10000.00) / 10000.00;
    }

    private void countAbsoluteAverage() {
        for (Double y : signal.values()
        ) {
            absoluteAverage += Math.abs(y);
        }
        absoluteAverage = absoluteAverage / (double) signal.size();
        absoluteAverage = Math.round(absoluteAverage * 10000.00) / 10000.00;
    }

    private void countRms() {
        for (Double y : signal.values()
        ) {
            rms += Math.pow(y, 2);
        }
        rms = rms / (double) signal.size();
        rms = Math.sqrt(rms);
        rms = Math.round(rms * 10000.00) / 10000.00;
    }

    private void countVariance() {
        for (Double y : signal.values()
        ) {
            variance += Math.pow(y - average, 2);
        }
        variance = variance / (double) signal.size();
        variance = Math.round(variance * 10000.00) / 10000.00;
    }

    private void countEffectiveValue() {
        effectiveValue = Math.sqrt(rms);
        effectiveValue = Math.round(effectiveValue * 10000.00) / 10000.00;
    }

    public BaseSignal addition(BaseSignal element) {
        float sumA = this.A + element.A, sumD = this.d, sumT1 = this.t1;
        if (element.t1 < sumT1) {
            sumT1 = element.t1;
        }
        if ((element.t1 + element.d) > this.t1 + this.d) {
            sumD = (element.t1 + element.d) - sumT1;
        }

        BaseSignal sum = new BaseSignal(sumA, sumT1, sumD);
        double x, y;
        for (double t = sumT1; Math.round(t * 100.00) / 100.00 <= (sumT1 + sumD); t += step) {
            x = Math.round(t * 100.00) / 100.00;
            y = signal.getOrDefault(x, 0.0d) + element.signal.getOrDefault(x, 0.0d);
            y = Math.round(y * 100000.00) / 100000.00;
            sum.signal.put(x, y);
        }

        sum.countAverage();
        sum.countAbsoluteAverage();
        sum.countRms();
        sum.countVariance();
        sum.countEffectiveValue();

        return sum;
    }

    public BaseSignal subtraction(BaseSignal element) {
        float sumA = this.A + element.A, sumD = this.d, sumT1 = this.t1;
        if (element.t1 < sumT1) {
            sumT1 = element.t1;
        }
        if ((element.t1 + element.d) > this.t1 + this.d) {
            sumD = (element.t1 + element.d) - sumT1;
        }

        BaseSignal sum = new BaseSignal(sumA, sumT1, sumD);
        double x, y;
        for (double t = sumT1; Math.round(t * 100.00) / 100.00 <= (sumT1 + sumD); t += step) {
            x = Math.round(t * 100.00) / 100.00;
            y = signal.getOrDefault(x, 0.0d) - element.signal.getOrDefault(x, 0.0d);
            y = Math.round(y * 100000.00) / 100000.00;
            sum.signal.put(x, y);
        }

        sum.countAverage();
        sum.countAbsoluteAverage();
        sum.countRms();
        sum.countVariance();
        sum.countEffectiveValue();

        return sum;
    }

    public BaseSignal multiplication(BaseSignal element) {
        float sumA = this.A + element.A, sumD = this.d, sumT1 = this.t1;
        if (element.t1 < sumT1) {
            sumT1 = element.t1;
        }
        if ((element.t1 + element.d) > this.t1 + this.d) {
            sumD = (element.t1 + element.d) - sumT1;
        }

        BaseSignal sum = new BaseSignal(sumA, sumT1, sumD);
        double x, y;
        for (double t = sumT1; Math.round(t * 100.00) / 100.00 <= (sumT1 + sumD); t += step) {
            x = Math.round(t * 100.00) / 100.00;
            y = signal.getOrDefault(x, 0.0d) * element.signal.getOrDefault(x, 0.0d);
            y = Math.round(y * 100000.00) / 100000.00;
            sum.signal.put(x, y);
        }

        sum.countAverage();
        sum.countAbsoluteAverage();
        sum.countRms();
        sum.countVariance();
        sum.countEffectiveValue();

        return sum;
    }

    public BaseSignal division(BaseSignal element) {
        float sumA = this.A + element.A, sumD = this.d, sumT1 = this.t1;
        if (element.t1 < sumT1) {
            sumT1 = element.t1;
        }
        if ((element.t1 + element.d) > this.t1 + this.d) {
            sumD = (element.t1 + element.d) - sumT1;
        }

        BaseSignal sum = new BaseSignal(sumA, sumT1, sumD);
        double x, y;
        for (double t = sumT1; Math.round(t * 100.00) / 100.00 <= (sumT1 + sumD); t += step) {
            x = Math.round(t * 100.00) / 100.00;
            if (element.signal.getOrDefault(x, 0.0d) != 0.0d) {
                y = signal.getOrDefault(x, 0.0d) / element.signal.getOrDefault(x, 0.0d);
            } else {
                y = signal.getOrDefault(x, 0.0d) / 0.00000000001d;
            }
            y = Math.round(y * 100000.00) / 100000.00;
            sum.signal.put(x, y);
        }

        sum.countAverage();
        sum.countAbsoluteAverage();
        sum.countRms();
        sum.countVariance();
        sum.countEffectiveValue();

        return sum;
    }

    public void saveToBinary(File file) throws Exception {
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

        writeParameters(out);
        out.close();
    }

    public void readFromBinary(File file) throws Exception {
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        readParameters(in);
        in.close();
    }

    private void writeParameters(DataOutputStream out) throws Exception {
        out.writeFloat(t1);
        out.writeFloat(step);
        out.writeFloat(d);
        out.writeBoolean(complex);

        for (double i = t1; Math.round(i * 100.00) / 100.00 <= t1 + d; i += step) {
            i = Math.round(i * 100.00) / 100.00;
            out.writeDouble(i);
            out.writeDouble(signal.get(i));
        }
    }

    private void readParameters(DataInputStream in) throws Exception {
        t1 = 0;
        step = 0;
        d = 0;
        complex = false;

        t1 = in.readFloat();
        step = in.readFloat();
        d = in.readFloat();
        complex = in.readBoolean();

        double x, y;
        signal = new HashMap<>();
        for (double i = t1; Math.round(i * 100.00) / 100.00 <= t1 + d; i += step) {
            x = in.readDouble();
            y = in.readDouble();
            if (y > A) {
                A = (float) y;
            }
            signal.put(x, y);
        }
        countAverage();
        countAbsoluteAverage();
        countRms();
        countVariance();
        countEffectiveValue();
    }

    public void getDataForHistogram(int numberOfParts) throws Exception {
        minValueHist = getMinValue();
        maxValueHist = getMaxValue();

        double amplitudeValue = Math.abs(minValueHist) + Math.abs(maxValueHist);
        double scope = amplitudeValue / numberOfParts;
        histogramTableValue = new int[numberOfParts];

        for (double val : signal.values()) {
            int index = checksScopeForValues(val, minValueHist, maxValueHist, scope, numberOfParts);
            histogramTableValue[index] += 1;
        }
    }

    private int checksScopeForValues(double val, double minValue, double maxValue, double scope, int numberOfParts) throws Exception {
        if (val == minValue) {
            return 0;
        } else if (val == maxValue) {
            return (numberOfParts - 1);
        }

        for (int i = 0; (minValue <= maxValue) || i <= (numberOfParts + 1); i++) {
            if (val <= (minValue + scope)) {

                return i;
            } else {
                minValue = minValue + scope; //przejście o zakres dalej
            }
        }

        throw new Exception("Błąd poza zakresem danych!");
    }

    private double getMinValue(){
        double minValue = signal.values().iterator().next();

        for(double val: signal.values()){
            minValue = val < minValue ?  val : minValue;
        }

        return minValue;
    }

    private double getMaxValue(){
        double maxValue = signal.values().iterator().next();

        for(double val: signal.values()){
            maxValue = val > maxValue ?  val : maxValue;
        }

        return maxValue;
    }

    public HashMap<Double, Double> sample(float samplingRate){
        HashMap<Double, Double> result = new HashMap<>();
        float t2 = t1 + d;
        double x, y;
        for (double t = t1; Math.round(t * 100.00) / 100.00 <= t2; t += samplingRate) {
            x = Math.round(t * 100.00) / 100.00;
            //y = Math.round(signalFunction(x) * 100000.00) / 100000.00;
            y = signal.get(x);
            result.put(x, y);
        }
        return result;
    }
}
