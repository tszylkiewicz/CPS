package sample;

import java.util.ArrayList;

public class Formula {

    public ArrayList<Double> generateX(float t1, float d) {
        ArrayList<Double> result = new ArrayList<>();
        float t2 = t1 + d;
        for (double t = t1; t <= t2; t += 0.01) {
            result.add(Math.round(t * 100.00) / 100.00);
        }
        return result;
    }

    public ArrayList<Double> sin(ArrayList<Double> X, float A, float T, float t1) {
        ArrayList<Double> Y = new ArrayList<>();
        for (Double x : X) {
            Y.add(A * Math.sin(((2.0d * Math.PI) / T) * (x - t1)));
        }
        return Y;
    }
}
