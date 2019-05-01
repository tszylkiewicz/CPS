package sample.Signals;

public class KroneckerImpulse extends BaseSignal {

    private double ns;

    public KroneckerImpulse(float A, float t1, float d, float f, float ns) {
        super(A, t1, d);
        this.step = f;
        this.ns = Math.round(ns * signalFrequency) / signalFrequency;
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
