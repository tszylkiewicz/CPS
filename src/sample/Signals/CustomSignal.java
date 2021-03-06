package sample.Signals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomSignal {

    public static BaseSignal getCustomSignal(int duration) throws Exception {
        SineWave sineWave1 = new SineWave(2.0f, 0, duration, 2.0f, 0, 16.0);
        sineWave1.generateSignal();

        SineWave sineWave2 = new SineWave(1.0f, 0, duration, 1.0f, 0, 16.0);
        sineWave2.generateSignal();

        SineWave sineWave3 = new SineWave(5.0f, 0, duration, 0.5f, 0, 16.0);
        sineWave3.generateSignal();

        BaseSignal bs = new BaseSignal();

        List<Double> sum = sumOfCollections(new ArrayList<>(sineWave1.signal.values()), new ArrayList<>(sineWave2.signal.values()), new ArrayList<>(sineWave3.signal.values()));

        int counter = 0;
        for (Map.Entry<Double, Double> entry : sineWave1.signal.entrySet()) {

            if (sineWave1.signal.size() > (counter + 1)) {
                bs.signal.put(entry.getKey(), sum.get(counter));
            }
            counter++;
        }

        return bs;

    }

    public static BaseSignal getCustomSignalS1(int duration) {
        SineWave sineWave1 = new SineWave(2.0f, 0, duration, 2.0f, 0, 16.0);
        sineWave1.generateSignal();

        BaseSignal bs = new BaseSignal();

        for (Map.Entry<Double, Double> entry : sineWave1.signal.entrySet()) {
            bs.signal.put(entry.getKey(), entry.getValue());
        }

        return bs;

    }

    public static BaseSignal getCustomSignalS2(int duration) {
        SineWave sineWave2 = new SineWave(1.0f, 0, duration, 1.0f, 0, 16.0);
        sineWave2.generateSignal();

        BaseSignal bs = new BaseSignal();

        for (Map.Entry<Double, Double> entry : sineWave2.signal.entrySet()) {
            bs.signal.put(entry.getKey(), entry.getValue());
        }

        return bs;

    }

    private static List<Double> sumOfCollections(ArrayList<Double> tab1, ArrayList<Double> tab2, ArrayList<Double> tab3) throws Exception {
        if (tab1.size() != tab2.size() || tab1.size() != tab3.size()) {
            throw new Exception("Błąd podczas sumowania kolekcji.");
        }

        List<Double> lista = new ArrayList<>();

        for (int i = 0; i < tab1.size(); i++) {
            lista.add(tab1.get(i) + tab2.get(i) + tab3.get(i));
        }
        return lista;
    }


}
