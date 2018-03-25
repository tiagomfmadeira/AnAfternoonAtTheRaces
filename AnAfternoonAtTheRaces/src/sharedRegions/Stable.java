package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;
import static main.SimulPar.K_numRaces;
import static main.SimulPar.N_numCompetitors;

/**
 * General description: Definition of the Stable information sharing region.
 */
public class Stable
{

    /**
     * Internal data
     */
    // array of flags indexed per race
    private final boolean[] proceedToPaddockFlag = new boolean[K_numRaces];
    // counter of horses that left the paddock per race
    private final int[] proceededHorsesCount = new int[K_numRaces];
    private final Logger logger;

    /**
     * Constructor
     *
     * @param logger General Repository of information, keeping a copy of the
     *               internal state of the problem
     */
    public Stable(Logger logger)
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
    public synchronized void summonHorsesToPaddock(int raceID)
    {

        proceedToPaddockFlag[raceID] = true;
        notifyAll();

        // change Broker state to ANNOUNCING_NEXT_RACE
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
        logger.setRaceNumber(raceID);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Horse/Jockey pair
    /**
     * Changes the horse/jockey pair state to AT_THE_STABLE and sleeps waiting
     * for a signal that the next race is starting.
     */
    public synchronized void proceedToStable()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());

        int raceId = hj.getRaceId();

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
     */
    public synchronized void proceedToStableFinal()
    {
        // change HorseJockey state to AT_THE_STABLE
        HorseJockey hj = ((HorseJockey) Thread.currentThread());
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_STABLE);
        int horseId = hj.getHorseJockeyID();
        int raceId = hj.getRaceId();
        logger.setHorseJockeyState(HorseJockeyState.AT_THE_STABLE, horseId, raceId);
    }
}
