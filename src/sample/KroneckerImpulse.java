package sample;

import java.util.Random;

public class KroneckerImpulse extends BaseSignal {

    public float f;
    public double ns;

    public KroneckerImpulse(float A, float t1, float d, float f, float ns) {
        super(A, t1, d);
        this.f = f;
        this.ns = Math.round(ns * 100.00) / 100.00;
    }

    @Override
    public double signalFunction(double x) {
        if (x == ns) {
            return A;
        } else {
            return 0;
        }
    }
}
