package sample.Signals;

import java.util.*;
import java.io.*;

public class BaseSignal {

    public float step = 0.001f; //1000Hz
    public double signalFrequency = 1000.00;
    private boolean complex = false;

    //Parameters
    public float A;
    public float t1;
    public float d;
    public TreeMap<Double, Double> signal;
    public double average;
    public double absoluteAverage;
    public double rms;
    public double variance;
    public double effectiveValue;

    //histogram parameters
    public int[] histogramTableValue;
    public double minValueHist;
    public double maxValueHist;

    public BaseSignal() {
        signal = new TreeMap<>();
    }

    public BaseSignal(float A, float t1, float d) {
        this.A = A;
        this.t1 = t1;
        this.d = d;
        signal = new TreeMap<>();
    }

    public BaseSignal(BaseSignal baseSignal) {
        this.A = baseSignal.A;
        this.t1 = baseSignal.t1;
        this.d = baseSignal.d;
        this.signal = baseSignal.signal;
    }

    /**
     * Public functions
     */
    public void generateSignal() {
        float t2 = t1 + d;
        double x, y;
        for (double t = t1; Math.round(t * signalFrequency) / signalFrequency <= t2; t += step) {
            x = Math.round(t * signalFrequency) / signalFrequency;
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
        for (double t = sumT1; Math.round(t * signalFrequency) / signalFrequency <= (sumT1 + sumD); t += step) {
            x = Math.round(t * signalFrequency) / signalFrequency;
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
        for (double t = sumT1; Math.round(t * signalFrequency) / signalFrequency <= (sumT1 + sumD); t += step) {
            x = Math.round(t * signalFrequency) / signalFrequency;
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
        for (double t = sumT1; Math.round(t * signalFrequency) / signalFrequency <= (sumT1 + sumD); t += step) {
            x = Math.round(t * signalFrequency) / signalFrequency;
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
        for (double t = sumT1; Math.round(t * signalFrequency) / signalFrequency <= (sumT1 + sumD); t += step) {
            x = Math.round(t * signalFrequency) / signalFrequency;
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

    public TreeMap<Double, Double> sample(float samplingRate) {
        TreeMap<Double, Double> result = new TreeMap<>();
        float t2 = t1 + d;
        double x, y;
        for (double t = t1; Math.round(t * signalFrequency) / signalFrequency <= t2; t += samplingRate) {
            x = Math.round(t * signalFrequency) / signalFrequency;
            y = signal.get(x);
            result.put(x, y);
        }
        return result;
    }

    public void quantify(int quantizationLevel, int typeOfQuantization) {
        TreeMap<Double, Double> result = new TreeMap<>();
        double x, y;
        double minValue = Collections.min(signal.values());
        double maxValue = Collections.max(signal.values());
        double quantizationWidth = (maxValue - minValue) / quantizationLevel;
        for (double t = t1; Math.round(t * signalFrequency) / signalFrequency <= (t1 + d); t += step) {
            x = Math.round(t * signalFrequency) / signalFrequency;

            if (typeOfQuantization == 0) {
                //z obcięciem
                y = quantizationWidth * (((int) (signal.get(x) / quantizationWidth)));
            } else {
                //z zaokrągleniem
                y = quantizationWidth * ((Math.round(signal.get(x) / quantizationWidth)));
            }
            result.put(x, y);
        }
        this.signal = result;
        //return result;
    }

    public BaseSignal ZOH() {
        TreeMap<Double, Double> resultSet = new TreeMap<>();
        double x;
        double y;
        double lastValue = this.signal.get(Collections.min(this.signal.keySet()));
        for (double t = t1; Math.round(t * signalFrequency) / signalFrequency <= (t1 + d); t += step) {
            x = Math.round(t * signalFrequency) / signalFrequency;
            if (this.signal.containsKey(x)) {
                lastValue = this.signal.get(x);
            }
            y = lastValue;
            resultSet.put(x, y);
        }
        BaseSignal result = new BaseSignal(A, t1, d);
        result.signal = resultSet;
        return result;
    }

    public BaseSignal SincInterpolation(double freq) {
        TreeMap<Double, Double> resultSet = new TreeMap<>();
        double x;
        double y;
        List<Double> sortedKeys = new ArrayList<>(this.signal.keySet());
        Collections.sort(sortedKeys);
        for (double t = t1; Math.round(t * signalFrequency) / signalFrequency <= (t1 + d); t += step) {
            x = Math.round(t * signalFrequency) / signalFrequency;
            y = 0;
            int c = 0;
            for (Double key : sortedKeys) {
                y += this.signal.get(key) * Sinc((x / freq) - c);
                c++;
            }
            resultSet.put(x, y);
        }
        BaseSignal result = new BaseSignal(A, t1, d);
        result.signal = resultSet;
        return result;
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

    /**
     * Private functions
     */
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

    private void writeParameters(DataOutputStream out) throws Exception {
        out.writeFloat(t1);
        out.writeFloat(step);
        out.writeFloat(d);
        out.writeBoolean(complex);

        for (double i = t1; Math.round(i * signalFrequency) / signalFrequency <= t1 + d; i += step) {
            i = Math.round(i * signalFrequency) / signalFrequency;
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
        signal = new TreeMap<>();
        for (double i = t1; Math.round(i * signalFrequency) / signalFrequency <= t1 + d; i += step) {
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

    private double getMinValue() {
        double minValue = signal.values().iterator().next();

        for (double val : signal.values()) {
            minValue = val < minValue ? val : minValue;
        }

        return minValue;
    }

    private double getMaxValue() {
        double maxValue = signal.values().iterator().next();

        for (double val : signal.values()) {
            maxValue = val > maxValue ? val : maxValue;
        }

        return maxValue;
    }

    private double Sinc(double x) {
        if (Math.round(x * 1000000.00) / 1000000.00 == 0.0) {
            return 1.0;
        } else {
            return Math.sin((Math.PI * x)) / (Math.PI * x);
        }
    }

    public TreeMap<Double, Double> convolute(BaseSignal secondSignal, boolean correlation) {
        float step;
        TreeMap<Double, Double> sig1 = new TreeMap();
        TreeMap<Double, Double> sig2 = new TreeMap();

        if (secondSignal.t1 < this.t1) {
            sig1.putAll(secondSignal.signal);
            if (correlation) {
                sig1 = new TreeMap<>(sig1.descendingMap());
            }
            sig2.putAll(this.signal);
            step = secondSignal.step;
        } else {
            sig1.putAll(this.signal);
            sig2.putAll(secondSignal.signal);
            if (correlation) {
                sig2 = new TreeMap<>(sig2.descendingMap());
            }
            step = this.step;
        }

        int size = sig1.keySet().size() + sig2.keySet().size() - 1;
        double t = sig1.firstKey();
        double[] resultKeys = new double[size];

        for (int i = 0; i < size; i++) {
            resultKeys[i] = t;
            t += step;
            t = Math.round(t * signalFrequency) / signalFrequency;
        }

        List<Double> values1 = new ArrayList(sig1.values());
        List<Double> values2 = new ArrayList(sig2.values());
        double[] resultValues = new double[size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= i; j++) {
                if ((values1.size() > j) && (values2.size() > i - j)) {
                    resultValues[i] += values1.get(j) * values2.get(i - j);
                }
            }
            resultValues[i] = Math.round(resultValues[i] * 100000.00) / 100000.00;
        }

        TreeMap<Double, Double> result = new TreeMap<>();
        for (int i = 0; i < size; i++) {
            result.put(resultKeys[i], resultValues[i]);
        }
        return result;
    }

    public TreeMap<Double, Double> correlate(BaseSignal secondSignal, boolean convolution) {
        if (convolution) {
            return this.convolute(secondSignal, true);
        }

        float step;
        TreeMap<Double, Double> sig1 = new TreeMap();
        TreeMap<Double, Double> sig2 = new TreeMap();

        if (secondSignal.t1 < this.t1) {
            sig1.putAll(secondSignal.signal);
            sig2.putAll(this.signal);
            step = secondSignal.step;
        } else {
            sig1.putAll(this.signal);
            sig2.putAll(secondSignal.signal);
            step = this.step;
        }

        int size = sig1.keySet().size() + sig2.keySet().size() - 1;
        double t = sig1.firstKey();
        double[] resultKeys = new double[size];

        for (int i = 0; i < size; i++) {
            resultKeys[i] = t;
            t += step;
            t = Math.round(t * signalFrequency) / signalFrequency;
        }

        List<Double> values1 = new ArrayList(sig1.values());
        List<Double> values2 = new ArrayList(sig2.values());
        double[] resultValues = new double[size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= i; j++) {
                if ((values1.size() > j) && (values2.size() > (values2.size() - 1 - Math.abs(j - i)) && (values2.size() - 1 - Math.abs(j - i) >= 0))) {
                    resultValues[i] += values1.get(j) * values2.get(values2.size() - 1 - Math.abs(j - i));
                }
            }
            resultValues[i] = Math.round(resultValues[i] * 100000.00) / 100000.00;
        }

        TreeMap<Double, Double> result = new TreeMap<>();
        for (int i = 0; i < size; i++) {
            result.put(resultKeys[i], resultValues[i]);
        }
        return result;
    }
}
