package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;
import genclass.GenericIO;
import java.util.HashSet;
import static main.SimulPar.M_numSpectators;

public class ControlCenter
{

    private boolean nextRaceStarted = false,
                                    nextRaceExists = true,
                                    lastSpectatorGoCheckHorses = false,
                                    raceHasEnded = false,
                                    reportedResults = false;

    private int spectatorsGoCheckHorsesCounter = 0,
                          spectatorsWatchedRaceCounter = 0;

    HashSet horseJockeysWinners = new HashSet();

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

         // wake up if next race starts or if race does not exit, broker determines this
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

    public synchronized boolean haveIWon(int horseJockey)
    {
            return horseJockeysWinners.contains(horseJockey);
    }

    public synchronized void relaxABit()
    {
        //  Change Spectator state to CELEBRATING
        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.CELEBRATING);
         GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm celebrating!");
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

    public synchronized void reportResults( HashSet horseJockeysDeclaredWinners )
    {
        horseJockeysWinners = horseJockeysDeclaredWinners;

        // wake up the spectators
        reportedResults = true;
        notifyAll();
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
