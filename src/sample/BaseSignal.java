package sample;

import java.util.HashMap;

public class BaseSignal {

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
        for (double t = t1; Math.round(t * 100.00) / 100.00 <= t2; t += 0.01) {
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

    public void countAverage() {
        for (Double y : signal.values()
        ) {
            average += y;
        }
        average = average / (double) signal.size();
    }

    public void countAbsoluteAverage() {
        for (Double y : signal.values()
        ) {
            absoluteAverage += Math.abs(y);
        }
        absoluteAverage = absoluteAverage / (double) signal.size();
    }

    public void countRms() {
        for (Double y : signal.values()
        ) {
            rms += Math.pow(y, 2);
        }
        rms = rms / (double) signal.size();
        rms = Math.sqrt(rms);
    }

    public void countVariance() {
        for (Double y : signal.values()
        ) {
            variance += Math.pow(y - average, 2);
        }
        variance = variance / (double) signal.size();
    }

    public void countEffectiveValue() {
        effectiveValue = Math.sqrt(rms);
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
        for (double t = sumT1; Math.round(t * 100.00) / 100.00 <= (sumT1 + sumD); t += 0.01) {
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
        for (double t = sumT1; Math.round(t * 100.00) / 100.00 <= (sumT1 + sumD); t += 0.01) {
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
        for (double t = sumT1; Math.round(t * 100.00) / 100.00 <= (sumT1 + sumD); t += 0.01) {
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
        for (double t = sumT1; Math.round(t * 100.00) / 100.00 <= (sumT1 + sumD); t += 0.01) {
            x = Math.round(t * 100.00) / 100.00;
            if(element.signal.getOrDefault(x, 0.0d) != 0.0d) {
                y = signal.getOrDefault(x, 0.0d) / element.signal.getOrDefault(x, 0.0d);
            } else {
                y = 0.0d;
            }
            y = Math.round(y * 100000.00) / 100000.00;
            sum.signal.put(x, y);
        }
        return sum;
    }
}
