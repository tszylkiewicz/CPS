package sample.Fourier;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class UtilsComplex {


    public static List<Complex> ConvertRealToComplex(ArrayList<Double> real) {
        List<Complex> ret = new ArrayList<>();

        for (Double val : real
        ) {
            ret.add(new Complex(val, 0));
        }


        return ret;
    }


}
