package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;
import stub.GeneralRepositoryStub;

import static main.SimulPar.M_numSpectators;

/**
 * General description: Definition of the Control Center/Watching Stand
 * information sharing region.
 */
public class ControlCenter
{

    /**
     * Internal data
     */
    private boolean nextRaceStarted = false,
            nextRaceExists = true,
            lastSpectatorGoCheckHorses = false,
            raceHasEnded = false,
            reportedResults = false;
    private int spectatorsGoCheckHorsesCounter = 0,
            spectatorsWatchedRaceCounter = 0;
    private boolean[] horseJockeysWinners;
    private final GeneralRepositoryStub logger;

    private boolean shutdownServer = false;

    /**
     * Constructor
     *
     * @param logger General Repository of information, keeping a copy of the
     *               internal state of the problem
     */
    public ControlCenter(GeneralRepositoryStub logger)
    {
        this.logger = logger;
    }

    ////////////////////////////////////////////////////////////////////////////
    // HorseJockey pair
    /**
     * Wake up the Spectators from the WAITING_FOR_A_RACE_TO_START blocking
     * state.
     */
    // called by the last horse/jockey pair to reach the paddock
    public synchronized void proceedToPaddock()
    {
        nextRaceStarted = true;
        notifyAll();
    }

    /**
     * Wake up the Broker from the SUPERVISING_THE_RACE blocking state.
     */
    // called by the last Horse/Jockey pair to cross the finish line
    public synchronized void makeAMove()
    {
        raceHasEnded = true;
        notifyAll();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Spectator
    /**
     * Changes the spectator state to WAITING_FOR_A_RACE_TO_START and sleeps
     * waiting for a signal that the next race is starting or that there are no
     * more races. Returns weather there's a next race or not.
     *
     * @return <code>true</code> if there is a next race; <code>false</code>
     *         otherwise
     */
    public synchronized boolean waitForNextRace(int specId)
    {

        logger.setSpectatorState(SpectatorState.WAITING_FOR_A_RACE_TO_START, specId);

        // wake up if next race starts or if race does not exit
        while (!nextRaceStarted && nextRaceExists)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }

        spectatorsGoCheckHorsesCounter++;

        // is the last spectator to go appraise the horses
        if (spectatorsGoCheckHorsesCounter == M_numSpectators)
        {
            // reset the var for next run
            nextRaceStarted = false;
            spectatorsGoCheckHorsesCounter = 0;
        }

        return nextRaceExists;
    }

    /**
     * Wake up the Broker from the SUPERVISING_THE_RACE blocking state.
     */
    public synchronized void lastToCheckHorses()
    {
        lastSpectatorGoCheckHorses = true;
        notifyAll();
    }

    /**
     * Change Spectator state to WATCHING_A_RACE and sleep waiting for a signal
     * that the race has ended.
     */
    public synchronized void goWatchTheRace(int specId)
    {
        //  Change Spectator state to WATCHING_A_RACE
        logger.setSpectatorState(SpectatorState.WATCHING_A_RACE, specId);

        while (!reportedResults)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }

        spectatorsWatchedRaceCounter++;

        // is the last spectator to leave for the betting center
        if (spectatorsWatchedRaceCounter == M_numSpectators)
        {
            // reset the var for next run
            reportedResults = false;
            spectatorsWatchedRaceCounter = 0;
        }
    }

    /**
     * Called by a Spectator to check whether the Horse/Jockey pair they bet on
     * won.
     *
     * @param horseJockey id of the Horse/Jockey pair the Spectator bet on
     *
     * @return <code>true</code> if the Horse/Jockey pair provided won the race;
     *         <code>false</code> otherwise
     */
    public synchronized boolean haveIWon(int horseJockey)
    {
        return horseJockeysWinners[horseJockey];
    }

    /**
     * Change the Spectator state to CELEBRATING. Final state of their life
     * cycle.
     */
    public synchronized void relaxABit(int specId)
    {

        logger.setSpectatorState(SpectatorState.CELEBRATING, specId);
    }

    ////////////////////////////////////////////////////////////////////////////
    //Broker
    /**
     * Sleeps waiting for a signal that all the Spectators have appraised the
     * horses.
     */
    public synchronized void summonHorsesToPaddock()
    {

        while (!lastSpectatorGoCheckHorses)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        // reset for next run
        lastSpectatorGoCheckHorses = false;
    }

    /**
     * Sleep waiting for a signal that the last Horse/Jockey pair has crossed
     * the finish line.
     */
    public synchronized void startTheRace()
    {

        while (!raceHasEnded)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        // reset the var for next run
        raceHasEnded = false;
    }

    /**
     * Place information on what Horse/Jockey pairs won the race in the shared
     * region. Wake up the spectators from WATCHING_A_RACE blocking state.
     *
     * @param horseJockeysDeclaredWinners array indicating for each Horse/Jockey
     *                                    pair if they have won the race,
     *                                    indexed by their ID
     */
    public synchronized void reportResults(boolean[] horseJockeysDeclaredWinners)
    {
        horseJockeysWinners = horseJockeysDeclaredWinners;

        // wake up the spectators
        reportedResults = true;
        notifyAll();
    }

    /**
     * Change the Broker state to PLAYING_HOST_AT_THE_BAR. Final state of their
     * life cycle.
     */
    public synchronized void entertainTheGuests()
    {
        // Change Broker state to PLAYING_HOST_AT_THE_BAR
        logger.setBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);

        nextRaceExists = false;
        notifyAll();
    }

    public synchronized void shutdown(){
        shutdownServer = true;
    }

    public synchronized boolean hasServiceFinished(){
        return shutdownServer;
    }
}
