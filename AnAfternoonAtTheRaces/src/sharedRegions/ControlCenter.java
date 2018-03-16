package sharedRegions;


import entities.Spectator;
import entities.SpectatorState;

// TODO: interface to exclude access from undesired entities
public class ControlCenter {

    private boolean nextRaceStarted;
    private boolean nextRaceExists;
    private final int M_numSpectators;
    private final int K_numRaces;
    private int spectatorsGoCheckHorsesCounter;


    public ControlCenter(int K_numRaces, int M_numSpectators){
        this.M_numSpectators = M_numSpectators;
        this.K_numRaces = K_numRaces;
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

        if(nextRaceExists)
            while (!nextRaceStarted) {
                try {
                    wait ();
                }
                catch (InterruptedException e) {}
            }

        return nextRaceExists;
    }

    public synchronized boolean goCheckHorses(){

        boolean isLastSpectator = false;

        spectatorsGoCheckHorsesCounter++;
        if(spectatorsGoCheckHorsesCounter == M_numSpectators){

            isLastSpectator = true;
            spectatorsGoCheckHorsesCounter = 0;
            nextRaceStarted = false;
        }

        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.APPRAISING_THE_HORSES);

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
