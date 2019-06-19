package sample.Fourier;


import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class DiscreteFourierTranform {

    public static List<Complex> Transform (List<Complex> values) throws Exception {
        int count = values.size();
        if ((count != 0) && ((count & (count - 1)) != 0))
        {
            throw new Exception("Number of values has to be power of 2");
        }
        List<Complex> ret = new ArrayList<>();

        for (int i = 0; i < count; i++)
        {
            ret.add(TransformValue(i, values).divide(count));
        }

        return ret;
    }

    private static Complex TransformValue(int m, List<Complex> values)
    {
        Complex ret = new Complex(0);

        int count = values.size();

        for (int i = 0; i < count; i++)
        {
            ret.add(new Complex(values.get(i).getReal(), values.get(i).getImaginary()).multiply(CoreFactor(m, i, count)));
        }

        return ret;
    }


    /**
     * Tutaj należy się upewnić żę funkcja się dobrze liczy.
     * @param m
     * @param n
     * @param N
     * @return
     */
    private static Complex CoreFactor(int m, int n, int N)
    {
        return new Complex(0, -2 * Math.PI * m * n / N).exp();
    }


}
