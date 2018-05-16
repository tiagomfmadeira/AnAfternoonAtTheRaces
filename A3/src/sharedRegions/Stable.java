package sharedRegions;

import entities.HorseJockeyState;
import interfaces.IGeneralRepository;

import java.rmi.RemoteException;

import static main.SimulPar.K_numRaces;
import static main.SimulPar.N_numCompetitors;

/**
 * General description: Definition of the Stable information sharing region.
 */
public class Stable implements interfaces.IStable {

    /**
     * Internal data
     */
    // array of flags indexed per race
    private final boolean[] proceedToPaddockFlag = new boolean[K_numRaces];
    // counter of horses that left the paddock per race
    private final int[] proceededHorsesCount = new int[K_numRaces];
    private final IGeneralRepository logger;
    private boolean shutdownServer = false;

    /**
     * Constructor
     *
     * @param logger General Repository of information, keeping a copy of the
     *               internal state of the problem
     */
    public Stable(IGeneralRepository logger)
    {
        this.logger = logger;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Broker
    /**
     * Wake up the horse/jockey pairs assigned to a race from the AT_THE_STABLE
     * blocking state.
     *
     * @param raceID ID of the race that the horse/jockey pairs to be awaken are
     *               assigned to
     */
    @Override
    public synchronized void summonHorsesToPaddock(int raceID) throws RemoteException
    {

        proceedToPaddockFlag[raceID] = true;
        notifyAll();

        // a change state log here may be nec
        logger.setRaceNumber(raceID);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Horse/Jockey pair
    /**
     * Changes the horse/jockey pair state to AT_THE_STABLE and sleeps waiting
     * for a signal that the next race is starting.
     *
     * @param raceId the ID of the race
     */
    @Override
    public synchronized void proceedToStable(int raceId)
    {

        // check the flag for this race
        while (!proceedToPaddockFlag[raceId])
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }

        proceededHorsesCount[raceId]++;

        // if is last horse to leave stable
        if (proceededHorsesCount[raceId] == N_numCompetitors)
        {
            // reset var, the horses for this race have left
            proceedToPaddockFlag[raceId] = false;
        }
    }

    /**
     * Changes the horse/jockey pair state to AT_THE_STABLE. Used as a symbolic
     * return to the stable, allowing the thread to finish its life cycle
     * instead of blocking again. This is used because the thread would never be
     * awoken again.
     *
     * @param horseId the ID of the Horse?jockey pair
     * @param raceId  the ID of the race
     */
    @Override
    public synchronized void proceedToStableFinal(int horseId, int raceId) throws RemoteException
    {
        // change HorseJockey state to AT_THE_STABLE
        logger.setHorseJockeyState(HorseJockeyState.AT_THE_STABLE, horseId, raceId);
    }

    /**
     * Changes a boolean variable state to true, symbolising the conclusion of
     * the service.
     */
    @Override
    public synchronized void shutdown()
    {
        shutdownServer = true;
    }

    /**
     * Checks whether the service has been completed.
     *
     * @return <code>true</code> if the service has been completed
     *         <code>false</code> otherwise
     */
    @Override
    public synchronized boolean hasServiceFinished()
    {
        return shutdownServer;
    }
}
