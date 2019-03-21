package sample;

import java.util.ArrayList;
import java.util.Random;

public class Formula {

    public ArrayList<Double> generateX(float t1, float d) {
        ArrayList<Double> result = new ArrayList<>();
        float t2 = t1 + d;
        for (double t = t1; Math.round(t * 100.00) / 100.00 <= t2; t += 0.01) {
            result.add(Math.round(t * 100.00) / 100.00);
        }
        return result;
    }

    public ArrayList<Double> sin(ArrayList<Double> X, float A, float T, float t1, int type) {
        ArrayList<Double> Y = new ArrayList<>();
        switch (type) {
            //Sine wave
            case 0: {
                for (Double x : X) {
                    double part = Math.sin(((2.0d * Math.PI) / T) * (x - t1));
                    Y.add(A * part);
                }
                break;
            }
            //Half-wave rectified sine
            case 1: {
                for (Double x : X) {
                    double part = Math.sin(((2.0d * Math.PI) / T) * (x - t1));
                    Y.add((1.0d / 2.0d) * (A * (part + Math.abs(part))));
                }
                break;
            }
            //Full-wave rectifier sine
            case 2: {
                for (Double x : X) {
                    double part = Math.sin(((2.0d * Math.PI) / T) * (x - t1));
                    Y.add(A * Math.abs(part));
                }
                break;
            }
            default: {
                break;
            }
        }
        return Y;
    }

    public ArrayList<Double> uniformNoise(ArrayList<Double> X, float A) {
        ArrayList<Double> Y = new ArrayList<>();
        double min = A * -1.0d;
        double max = A;
        for (Double ignored : X) {
            Y.add(min + new Random().nextDouble() * (max - min));
        }
        return Y;
    }

    public ArrayList<Double> gaussianNoise(ArrayList<Double> X, float A) {
        ArrayList<Double> Y = new ArrayList<>();
        double min = A * -1.0d;
        double max = A;
        Random r = new Random();
        for (Double x : X) {
            //double temp = (1.0d/(Math.sqrt(2.0d * Math.PI)))*Math.pow(Math.E, (-Math.pow(x,2))/2.0d);
            //double temp = r.nextGaussian();
            //System.out.println(temp);
            Y.add(r.nextGaussian());
        }
        return Y;
    }

}
