package sample.Fourier;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaveletTransform {


    private static List<Double> H = new ArrayList<>(Arrays.asList(0.32580343,
            1.01094572,
            0.8922014,
            -0.03957503,
            -0.26450717,
            0.0436163,
            0.0465036,
            -0.01498699));

    private static List<Double> G = new ArrayList<>(Arrays.asList(H.get(7),
            -H.get(6),
            H.get(5),
            -H.get(4),
            H.get(3),
            -H.get(2),
            H.get(1),
            -H.get(0)));


    public static List<Complex> WaveletTransformation(List<Double> points) {
        List<Complex> result = new ArrayList<>();

        List<Double> hSamples = ConvoluteSignals(points, H).subList(0, points.size());
        List<Double> gSamples = ConvoluteSignals(points, G).subList(0, points.size());

        List<Double> hHalf = new ArrayList<>();
        List<Double> gHalf = new ArrayList<>();

        for (int i = 0; i < hSamples.size(); i++) {
            if (i % 2 == 0) {
                hHalf.add(hSamples.get(i));
            } else {
                gHalf.add(gSamples.get(i));
            }
        }

        for (int i = 0; i < gHalf.size(); i++)
            result.add(new Complex(hHalf.get(i), gHalf.get(i)));

        return result;
    }


    public static List<Double> WaveletReverseTransformation(List<Complex> points) {
        List<Double> hRevesed = reverseArray(H);
        List<Double> gReversed = reverseArray(G);

        List<Double> hSamples = new ArrayList<>();
        List<Double> gSamples = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            hSamples.add(points.get(i).getReal());
            hSamples.add(0.0);

            gSamples.add(0.0);
            gSamples.add(points.get(i).getImaginary());
        }

        List<Double> hResult = ConvoluteSignals(hSamples, hRevesed).subList(0, hSamples.size());
        List<Double> gResult = ConvoluteSignals(gSamples, gReversed).subList(0, gSamples.size());

        return AddSignals(hResult, gResult);
    }

    public static List<Double> AddSignals(List<Double> signal1, List<Double> signal2) {
        List<Double> result = new ArrayList<>();

        for (int i = 0; i < Math.max(signal1.size(), signal2.size()); i++) {

            result.add(signal1.get(i) + signal2.get(i));
        }

        return result;
    }


    /**
     * Reverse Array
     *
     * @param regularArray
     * @return
     */
    private static List<Double> reverseArray(List<Double> regularArray) {
        int size = regularArray.size()-1;

        List<Double> reverseArray = new ArrayList<>();

        for (int i = size; i >= 0; i--) {
            reverseArray.add(regularArray.get(i));
        }

        return reverseArray;

    }


    public static List<Double> ConvoluteSignals(List<Double> signal1, List<Double> signal2) {
        List<Double> result = new ArrayList<>();

        for (int i = 0; i < signal1.size() + signal2.size() - 1; i++) {
            double sum = 0;
            for (int j = 0; j < signal1.size(); j++) {
                if (i - j < 0 || i - j >= signal2.size())
                    continue;

                sum += signal1.get(j) * signal2.get(i - j);
            }
            result.add(sum);
        }

        return result;
    }

}



