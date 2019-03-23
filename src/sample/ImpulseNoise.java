package sample;


import java.util.Random;

public class ImpulseNoise extends BaseSignal {

    public float f;
    public float p;

    public ImpulseNoise(float A, float t1, float d, float f, float p) {
        super(A, t1, d);
        this.f = f;
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
