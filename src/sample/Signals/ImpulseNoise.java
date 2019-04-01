package sample.Signals;


import java.util.Random;

public class ImpulseNoise extends BaseSignal {

    private float p;

    public ImpulseNoise(float A, float t1, float d, float f, float p) {
        super(A, t1, d);
        this.step = f;
        this.p = p;
    }

    @Override
    public double signalFunction(double x) {
        Random random = new Random();
        double rand = random.nextDouble();
        if(rand < p) {
            return A;
        } else {
            return 0;
        }
    }
}
