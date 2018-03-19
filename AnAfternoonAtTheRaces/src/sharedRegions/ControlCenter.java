package sharedRegions;


import entities.Spectator;
import entities.SpectatorState;
import main.SimulPar;

// TODO: interface to exclude access from undesired entities
public class ControlCenter {

    private boolean nextRaceStarted;
    private boolean nextRaceExists;
    private int spectatorsGoCheckHorsesCounter;


    public ControlCenter(){
        this.nextRaceStarted = false;
        this.nextRaceExists = true;
    }

    //HorseJockey
    //Only called by last horse to leave the stable
    public synchronized void proceedToPaddock(){
        nextRaceStarted = true;
        notifyAll();
    }

    //Spectator
    public synchronized boolean waitForNextRace(){

        if(nextRaceExists) {

            //WFR
            while (!nextRaceStarted) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }

            ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.APPRAISING_THE_HORSES);

        }

        return nextRaceExists;
    }

    public synchronized boolean goCheckHorses(){

        boolean isLastSpectator = false;

        spectatorsGoCheckHorsesCounter++;

        if(spectatorsGoCheckHorsesCounter == SimulPar.M_numSpectators){

            isLastSpectator = true;
            spectatorsGoCheckHorsesCounter = 0;
            nextRaceStarted = false;
        }

        return isLastSpectator;
    }

    public synchronized void goWatchTheRace(){}

    public synchronized boolean haveIWon(){}

    public synchronized void relaxABit(){}


    //Broker
    public synchronized void acceptTheBets(){}

    public synchronized void startTheRace(){}

    public synchronized void reportResults(){}

    public synchronized void entertainTheGuests(){}
}
