package sample.Controllers;

import sample.Signals.CustomSignal;

public class FourierController {

public void getCustomSignal(){

    CustomSignal cs = new CustomSignal();
    try {
        cs.getCustomSignal(4);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
