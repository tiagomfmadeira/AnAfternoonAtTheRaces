package sharedRegions;

import java.util.List;

public class Stable {

    private boolean proceedToStableFlag = false;

    public synchronized boolean proceedToStable() {

        while (!proceedToStableFlag) {
            try {
                wait ();
            }
            catch (InterruptedException e) {}
        }


    }

    public synchronized void summonHorsesToPaddock(int k) {


    }





}
