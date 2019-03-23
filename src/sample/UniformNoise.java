package sample;

import java.util.Random;

public class UniformNoise extends BaseSignal {

    public int type;

    public UniformNoise(float A, float t1, float d, int type) {
        super(A, t1, d);
        this.type = type;
    }

    @Override
    public double signalFunction(double x) {
        switch (type) {
            //Uniform noise
            case 0: {
                return -A + new Random().nextDouble() * (2.0d * A);
            }
            //Gaussian noise
            case 1: {
                Random r = new Random();
                return r.nextGaussian();
            }
            default: {
                return x;
            }
        }
    }
}
