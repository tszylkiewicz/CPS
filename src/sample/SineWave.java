package sample;

import java.util.HashMap;

public class SineWave extends BaseSignal {

    public float T;
    public int type;

    public SineWave(float A, float t1, float d, float T, int type) {
        super(A, t1, d);
        this.T = T;
        this.type = type;
    }

    @Override
    public double signalFunction(double x) {
        double part = Math.sin(((2.0d * Math.PI) / T) * (x - t1));
        switch (type) {
            //Sine wave
            case 0: {
                return A * part;
            }
            //Half-wave rectified sine
            case 1: {
                return (1.0d / 2.0d) * (A * (part + Math.abs(part)));
            }
            //Full-wave rectifier sine
            case 2: {
                return A * Math.abs(part);
            }
            default: {
                return x;
            }
        }
    }
}

