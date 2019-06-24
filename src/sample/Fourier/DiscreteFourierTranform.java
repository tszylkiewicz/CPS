package sample.Fourier;


import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class DiscreteFourierTranform {

    public static List<Complex> Transform(List<Complex> values) throws Exception {
        int count = values.size();
        if ((count != 0) && ((count & (count - 1)) != 0)) {
            throw new Exception("Number of values must be power of 2.");
        }
        List<Complex> ret = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            ret.add(TransformValue(i, values).divide(count));
        }

        return ret;
    }

    private static Complex TransformValue(int m, List<Complex> values) {
        Complex ret = new Complex(0, 0);

        int count = values.size();

        for (int i = 0; i < count; i++) {
            ret = ret.add(new Complex(values.get(i).getReal(), values.get(i).getImaginary()).multiply(UtilsComplex.coreFactor(m, i, count)));
        }

        return ret;
    }


    public static List<Double> TransformBack(List<Complex> values) throws Exception {
        int count = values.size();
        if ((count != 0) && ((count & (count - 1)) != 0)) {
            throw new Exception("Number of values must be power of 2.");
        }
        List<Double> ret = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            ret.add(TransformValueBack(i, values));
        }

        return ret;

    }


    private static double TransformValueBack(int m, List<Complex> values) {
        Double ret = new Double(0);
        int count = values.size();
        for (int i = 0; i < count; i++) {
            ret += (values.get(i).multiply(UtilsComplex.reverseCoreFactor(m, i, count))).getReal();
        }

        return ret;
    }


}


