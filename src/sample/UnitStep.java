package sample;

public class UnitStep extends BaseSignal {

    public float ts;

    public UnitStep(float A, float t1, float d, float ts) {
        super(A, t1, d);
        this.ts = ts;
    }

    @Override
    public double signalFunction(double x) {
        if(x > ts) {
            return A;
        } else if(x == ts) {
            return 0.5d * A;
        } else {
            return 0;
        }
    }
}
