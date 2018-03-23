package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;

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

    boolean [ ] horseJockeysWinners;

    private Logger logger;

    public ControlCenter(Logger logger){
        this.logger = logger;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // HorseJockey

    // Last horse to leave the Stable wakes up the Spectators
    public synchronized void proceedToPaddock()
    {
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
        Spectator spec =  ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();
        spec.setSpectatorState(SpectatorState.WAITING_FOR_A_RACE_TO_START);
        logger.setSpectatorState(SpectatorState.WAITING_FOR_A_RACE_TO_START, specId);
        logger.setMoneyAmount(spec.getWalletValue(),specId);

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
        lastSpectatorGoCheckHorses = true;
        notifyAll();
    }

    public synchronized void goWatchTheRace()
    {
        //  Change Spectator state to WATCHING_A_RACE
        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.WATCHING_A_RACE);
        logger.setSpectatorState(SpectatorState.WATCHING_A_RACE,
                ((Spectator) Thread.currentThread()).getSpectatorID());

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
            return horseJockeysWinners[ horseJockey ];
    }

    public synchronized void relaxABit()
    {
        //  Change Spectator state to CELEBRATING
        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.CELEBRATING);
        logger.setSpectatorState(SpectatorState.CELEBRATING, ((Spectator) Thread.currentThread()).getSpectatorID() );
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Broker

    public synchronized void summonHorsesToPaddock()
    {
        // Change Broker state to ANNOUNCING_NEXT_RACE
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
        logger.setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);

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

    }

    public synchronized void startTheRace()
    {
        // Change Broker state to SUPERVISING_THE_RACE
        ((Broker)Thread.currentThread()).setBrokerState(BrokerState.SUPERVISING_THE_RACE);
        logger.setBrokerState(BrokerState.SUPERVISING_THE_RACE);

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

    }

    public synchronized void reportResults(boolean [ ] horseJockeysDeclaredWinners )
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
        logger.setBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);

        nextRaceExists = false;
        notifyAll();
    }
}
