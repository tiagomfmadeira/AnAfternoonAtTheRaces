package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;
import genclass.GenericIO;
import main.SimulPar;

// TODO: interface to exclude access from undesired entities
public class ControlCenter
{

    private boolean nextRaceStarted;
    private boolean nextRaceExists;

    private boolean lastSpectatorGoCheckHorses = false;
    private boolean raceHasEnded = false;

    private int spectatorsGoCheckHorsesCounter;


    public ControlCenter()
    {
        this.nextRaceStarted = false;
        this.nextRaceExists = true;
    }

    // HorseJockey
    // Last horse to leave the Stable wakes up the Spectators
    public synchronized void proceedToPaddock()
    {
        GenericIO.writelnString(Thread.currentThread().getName() +  " says: The spectators can come see us!");
        nextRaceStarted = true;
        notifyAll();
    }

    // Spectator
    public synchronized boolean waitForNextRace()
    {
        // Changed by the Broker
        return nextRaceExists;
    }

    public synchronized boolean goCheckHorses()
    {
        boolean isLastSpectator = false;

        while (!nextRaceStarted)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {

            }
        }

        GenericIO.writelnString(Thread.currentThread().getName() +  " says: Time to appraise the horses!");

        spectatorsGoCheckHorsesCounter++;

        if(spectatorsGoCheckHorsesCounter == SimulPar.M_numSpectators)
        {
            GenericIO.writelnString(Thread.currentThread().getName() +  " says: I'm the Spectator last to appraise horses!");
            isLastSpectator = true;
            spectatorsGoCheckHorsesCounter = 0;
            nextRaceStarted = false;
            lastSpectatorGoCheckHorses = true;
            notifyAll();
        }
        return isLastSpectator;
    }

    public synchronized void goWatchTheRace()
    {
        //  Change Spectator state to WATCHING_A_RACE
        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.WATCHING_A_RACE);
         GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm at the Control Center to watch the race!");

          while (!raceHasEnded)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {

            }
        }

    }

    public synchronized boolean haveIWon()
    {
        return false;
    }

    public synchronized void relaxABit()
    {

    }

    //Broker
    public synchronized void summonHorsesToPaddock()
    {
        // Change Broker state to ANNOUNCING_NEXT_RACE
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);

        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm sleeping at the Control Center!");

        while (!lastSpectatorGoCheckHorses)
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }

        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm off to the Betting Center!");
    }

    public synchronized void startTheRace()
    {

    }

    public synchronized void reportResults()
    {

    }

    public synchronized void entertainTheGuests()
    {
        this.nextRaceExists = false;
    }
}
