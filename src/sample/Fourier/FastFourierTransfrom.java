package sample.Fourier;

import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class FastFourierTransfrom {

    public static List<Complex> FastFourierTransform(List<Complex> complexes) {
        int count = complexes.size();

        List<Complex> transformed = SwitchSamples(complexes, false);

        List<Complex> afterDivede = new ArrayList<>();

        for (Complex cmp : transformed
        ) {
            afterDivede.add(cmp.divide(count));
        }

        return afterDivede;

    }

    public static List<Double> FastFourierReverseTransformation(List<Complex> points) {
        List<Complex> transformed = SwitchSamples(points, true);

        List<Double> afterChange = new ArrayList<>();

        for (Complex cmp : transformed
        ) {
            afterChange.add(cmp.getReal());
        }

        return afterChange;
    }

    private static List<Complex> SwitchSamples(List<Complex> points, boolean reverse) {
        if (points.size() < 2)
            return points;

        List<Complex> odd = new ArrayList<>();
        List<Complex> even = new ArrayList<>();

        for (int i = 0; i < points.size() / 2; i++) {
            even.add(points.get(i * 2));
            odd.add(points.get(i * 2 + 1));
        }

        List<Complex> result = Connect(SwitchSamples(even, reverse), SwitchSamples(odd, reverse), reverse);

        return result;
    }

    private static List<Complex> Connect(List<Complex> evenPoints, List<Complex> oddPoints, boolean reverse) {
        List<Complex> result = new ArrayList<>();
        List<Complex> resultRight = new ArrayList<>();

        for (int i = 0; i < oddPoints.size(); i++) {
            if (!reverse) {
                result.add(evenPoints.get(i).add(UtilsComplex.coreFactor(i, 1, oddPoints.size() * 2).multiply(oddPoints.get(i))));
                resultRight.add(evenPoints.get(i).subtract(UtilsComplex.coreFactor(i, 1, oddPoints.size() * 2).multiply(oddPoints.get(i))));
            } else {
                result.add(evenPoints.get(i).add(UtilsComplex.reverseCoreFactor(i, 1, oddPoints.size() * 2).multiply(oddPoints.get(i))));
                resultRight.add(evenPoints.get(i).subtract(UtilsComplex.reverseCoreFactor(i, 1, oddPoints.size() * 2).multiply(oddPoints.get(i))));
            }
        }

        result.addAll(resultRight);

        return result;
    }

}
