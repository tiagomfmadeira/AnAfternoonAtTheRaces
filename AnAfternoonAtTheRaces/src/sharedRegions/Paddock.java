package sharedRegions;

import entities.*;
import main.SimulPar;

import static main.SimulPar.N_numCompetitors;

// TODO: interface to exclude access from undesired entities
public class Paddock {

    // Check when turns to false again
    private boolean lastSpectatorGoCheckHorses = false;
    private boolean lastHorseProceedToStartLine = false;
    private int proceededHorsesCount = 0;



    //HorseJockey
    public synchronized void proceedToPaddock() {

        while (!lastSpectatorGoCheckHorses) {
            try {
                wait ();
            }
            catch (InterruptedException e) {}
        }



    }

    public synchronized boolean proceedToStartLine() {

        boolean isLastHorse = false;

        proceededHorsesCount++;

        if(proceededHorsesCount == N_numCompetitors){
            isLastHorse = true;
            proceededHorsesCount = 0;
        }

        return isLastHorse;
    }

    public synchronized void lastProceedToStartLine() {
        lastHorseProceedToStartLine = true;
        notifyAll();
    }


    //Spectator
    public synchronized void lastToCheckHorses() {
        lastSpectatorGoCheckHorses = true;
        notifyAll();
    }


    public synchronized void goCheckHorses() {

        //ATR
        while (!lastHorseProceedToStartLine) {
            try {
                wait ();
            }
            catch (InterruptedException e) {}
        }

        ((Spectator)Thread.currentThread()).setSpectatorState(SpectatorState.PLACING_A_BET);

    }

    //Broker
    public synchronized void summonHorsesToPaddock() {

        //ANR
        while (!lastSpectatorGoCheckHorses) {
            try {
                wait ();
            }
            catch (InterruptedException e) {}
        }

        ((Broker)Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);
    }


}
