package sample;

import javafx.fxml.FXML;

import java.util.HashMap;
import java.io.*;

public class BaseSignal {

    protected float step = 0.01f;
    public boolean complex = false;

    //Parameters
    public float A;
    public float t1;
    public float d;
    public HashMap<Double, Double> signal;
    public double average;
    public double absoluteAverage;
    public double rms;
    public double variance;
    public double effectiveValue;

    public BaseSignal(float A, float t1, float d) {
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

    @FXML
    public void countAverage() {
        for (Double y : signal.values()
        ) {
            average += y;
        }
        average = average / (double) signal.size();
        average = Math.round(average * 10000.00) / 10000.00;
    }

    public void countAbsoluteAverage() {
        for (Double y : signal.values()
        ) {
            absoluteAverage += Math.abs(y);
        }
        absoluteAverage = absoluteAverage / (double) signal.size();
        absoluteAverage = Math.round(absoluteAverage * 10000.00) / 10000.00;
    }

    public void countRms() {
        for (Double y : signal.values()
        ) {
            rms += Math.pow(y, 2);
        }
        rms = rms / (double) signal.size();
        rms = Math.sqrt(rms);
        rms = Math.round(rms * 10000.00) / 10000.00;
    }

    public void countVariance() {
        for (Double y : signal.values()
        ) {
            variance += Math.pow(y - average, 2);
        }
        variance = variance / (double) signal.size();
        variance = Math.round(variance * 10000.00) / 10000.00;
    }

    public void countEffectiveValue() {
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
                y = 0.0d;
            }
            y = Math.round(y * 100000.00) / 100000.00;
            sum.signal.put(x, y);
        }
        return sum;
    }

    public void saveToBinary(String name) throws Exception {
        DataOutputStream out;
        File file = new File("test.bin");
        out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

        writeParameters(out);
        out.close();
    }

    public void writeParameters(DataOutputStream out) throws Exception {
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

    public void readParameters(DataInputStream in) throws Exception {
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

    }


    public void readFromBinary(String name) throws Exception {
        File file = new File("test.bin");
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

        readParameters(in);
        in.close();
    }
}
