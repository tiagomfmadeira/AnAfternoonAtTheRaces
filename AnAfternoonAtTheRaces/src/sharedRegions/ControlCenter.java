package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;
import genclass.GenericIO;
import static main.SimulPar.M_numSpectators;

// TODO: interface to exclude access from undesired entities
public class ControlCenter
{

    private boolean nextRaceStarted;
    private boolean nextRaceExists;

    private boolean lastSpectatorGoCheckHorses = false;

    private boolean raceHasEnded = false;

    private boolean reportedResults = false;

    private int spectatorsGoCheckHorsesCounter = 0;
    private int spectatorsWatchedRaceCounter = 0;


    public ControlCenter()
    {
        this.nextRaceStarted = false;
        this.nextRaceExists = true;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HorseJockey

    // Last horse to leave the Stable wakes up the Spectators
    public synchronized void proceedToPaddock()
    {
        GenericIO.writelnString(Thread.currentThread().getName() +  " says: The spectators can come see us!");
        nextRaceStarted = true;
        notifyAll();
    }

    public synchronized void makeAMove()
    {
        // wake up the broker
        //called by last horse to cross the finish line

        raceHasEnded = true;
        notifyAll();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Spectator
    public synchronized boolean waitForNextRace()
    {
         GenericIO.writelnString(Thread.currentThread().getName() +  " says: Waiting for next race!");

         // breaks if nextRace does not exit, broker determines this
        while (!nextRaceStarted && nextRaceExists)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {

            }
        }

        // Changed by the Broker
        return nextRaceExists;
    }

    public synchronized void goCheckHorses()
    {
        GenericIO.writelnString(Thread.currentThread().getName() +  " says: I'm headed to the Paddock!");

        spectatorsGoCheckHorsesCounter++;

        if(spectatorsGoCheckHorsesCounter == M_numSpectators)       // the last spectator to leave the controlcenter
        {
            // reset the var for next run
            nextRaceStarted = false;
            spectatorsGoCheckHorsesCounter = 0;
        }
    }

    public synchronized void lastToCheckHorses()
    {
        GenericIO.writelnString(Thread.currentThread().getName() +  " says: The Broker can go accept the bets!");
        lastSpectatorGoCheckHorses = true;
        notifyAll();
    }

    public synchronized void goWatchTheRace()
    {
        //  Change Spectator state to WATCHING_A_RACE
        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.WATCHING_A_RACE);
         GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm at the Control Center to watch the race!");

          while (!reportedResults)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {

            }
        }

        spectatorsWatchedRaceCounter++;

        if(spectatorsWatchedRaceCounter == M_numSpectators)       // the last spectator to leave the controlcenter
        {
            // reset the var for next run
            reportedResults = false;
            spectatorsWatchedRaceCounter = 0;
        }
    }

    public synchronized boolean haveIWon()
    {
        return false;
    }

    public synchronized void relaxABit()
    {

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        // reset for next run
        lastSpectatorGoCheckHorses = false;

        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm off to the Betting Center!");
    }

    public synchronized void startTheRace()
    {
        // Change Broker state to SUPERVISING_THE_RACE
        ((Broker)Thread.currentThread()).setBrokerState(BrokerState.SUPERVISING_THE_RACE);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm supervising the race!");

        while (!raceHasEnded)
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }
        // reset the var for next run
        raceHasEnded = false;

        GenericIO.writelnString(Thread.currentThread().getName() + " I have finished watching the race!");
    }

    public synchronized void reportResults()
    {
        // TODO fill in information about the winner

        // wake up the spectators
        reportedResults = true;
        notifyAll();
    }

    public synchronized boolean areThereAnyWinners()
    {
        return false;
    }

    public synchronized void entertainTheGuests()
    {
        // Change Broker state to PLAYING_HOST_AT_THE_BAR
        ((Broker)Thread.currentThread()).setBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm playing host at the bar!");

        nextRaceExists = false;
        notifyAll();
    }
}
