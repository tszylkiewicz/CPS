package sample.Signals;

public class SquareWave extends BaseSignal {

    private float T;
    private float kw;
    private int type;

    public SquareWave(float A, float t1, float d, float T, float kw, int type) {
        super(A, t1, d);
        this.T = T;
        this.kw = kw;
        this.type = type;
    }

    @Override
    public double signalFunction(double x) {
        int k = 0;
        int periods = (int) Math.floor(d / T);
        for (int i = 0; i <= periods; i++) {
            if (x >= (T * i + t1) && x < (T * (i + 1) + t1)) {
                k = i;
            }
        }
        switch (type) {
            //Square wave
            case 0: {
                if (x >= (k * T + t1) && x < (kw * T + k * T + t1)) {
                    return A;
                } else {
                    return 0;
                }

            }
            //Symmetrical  square wave
            case 1: {
                if (x >= (k * T + t1) && x < (kw * T + k * T + t1)) {
                    return A;
                } else {
                    return -A;
                }
            }
            //Triangle wave
            case 2: {
                if (x >= (k * T + t1) && x < (kw * T + k * T + t1)) {
                    return (A / (kw * T)) * (x - k * T - t1);
                } else {
                    return (((-A) / (T * (1.0d - kw))) * (x - k * T - t1)) + (A / (1.0d - kw));
                }
            }
            default: {
                return x;
            }
        }
    }
}
