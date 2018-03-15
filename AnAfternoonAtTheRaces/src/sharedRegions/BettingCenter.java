package sharedRegions;

// TODO: interface to exclude access from undesired entities
public class BettingCenter {


    //Spectator
    public synchronized boolean placeABet() {
    }

    public synchronized void lastToPlaceBet() {
    }

    public synchronized void goCollectTheGains() {
    }


    //Broker
    public synchronized boolean areThereAnyWinners() {
    }

    public synchronized void honourTheBets() {
    }



}
