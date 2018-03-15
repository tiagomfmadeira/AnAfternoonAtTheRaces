package sharedRegions;

// TODO: interface to exclude access from undesired entities
public class Paddock {

    //HorseJockey
    public synchronized void proceedToPaddock() {
    }

    public synchronized boolean proceedToStartLine() {
    }

    public synchronized void lastProceedToStartLine() {
    }

    //Spectator
    public synchronized void lastToCheckHorses() {
    }

    public synchronized void goCheckHorses() {
    }

    //Broker
    public synchronized void summonHorsesToPaddock() {
    }


}
