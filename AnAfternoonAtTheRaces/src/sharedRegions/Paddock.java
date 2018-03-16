package sharedRegions;

// TODO: interface to exclude access from undesired entities
public class Paddock {


    private boolean lastSpectatorgoCheckHorses = false;

    //HorseJockey
    public synchronized void proceedToPaddock() {

        while (!lastSpectatorgoCheckHorses) {
            try {
                wait ();
            }
            catch (InterruptedException e) {}
        }
    }

    public synchronized boolean proceedToStartLine() {
    }

    public synchronized void lastProceedToStartLine() {
    }


    //Spectator
    public synchronized void lastToCheckHorses() {
        lastSpectatorgoCheckHorses = true;
        notifyAll();
    }

    public synchronized void goCheckHorses() {
    }

    //Broker
    public synchronized void summonHorsesToPaddock() {
    }


}
