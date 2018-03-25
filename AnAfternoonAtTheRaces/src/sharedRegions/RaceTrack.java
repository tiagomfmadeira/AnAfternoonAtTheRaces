package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;
import main.SimulPar;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import static main.SimulPar.N_numCompetitors;

/**
 * General description: Definition of the Race Track information sharing region.
 */
public class RaceTrack
{

    /**
     * Internal data
     */
    private final int[] distance;
    private final boolean[] raceTurn = new boolean[SimulPar.N_numCompetitors];
    private final boolean[] crossedFinish = new boolean[SimulPar.N_numCompetitors];
    private final int[] racePosition = new int[SimulPar.N_numCompetitors];
    private final int[] iterationCounter = new int[SimulPar.N_numCompetitors];
    private boolean raceEnded = true;
    private int finishLineCount = 0;
    private final Logger logger;

    /**
     * Constructor
     *
     * @param distance array containing the length of the track for each race to
     *                 occur
     * @param logger   General Repository of information, keeping a copy of the
     *                 internal state of the problem
     */
    public RaceTrack(int[] distance, Logger logger)
    {
        //later this should be an array or something
        this.distance = distance;
        this.logger = logger;
        this.logger.setDistanceInRace(distance);
    }

    ////////////////////////////////////////////////////////////////////////////
    //Horse/Jockey pair
    /**
     * Changes the Horse/Jockey pair state to AT_THE_START_LINE and sleeps
     * waiting for a signal from the Broker that the race has started, or a
     * signal from a move of another Horse/Jockey pair. When woken up changes
     * the state of the Horse/Jockey pair to RUNNING.
     */
    public synchronized void proceedToStartLine()
    {
        //  Change HorseJockey state to AT_THE_START_LINE
        HorseJockey hj = (HorseJockey) Thread.currentThread();
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE);
        int horseJockeyId = hj.getHorseJockeyID();
        int raceId = hj.getRaceId();

        logger.setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE,
                horseJockeyId, raceId);

        while (!raceTurn[horseJockeyId])
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        raceTurn[horseJockeyId] = false;

        // Change Horse/Jockey state to RUNNING
        hj.setHorseJockeyState(HorseJockeyState.RUNNING);
        logger.setHorseJockeyState(HorseJockeyState.RUNNING, horseJockeyId, raceId);
    }

    /**
     * The Horse/Jockey pair blocks after carrying out a position increment,
     * unless he crosses the finishing line. Check if the Horse/Jockey pair has
     * crossed the finish line, in which case it records that it's standing in
     * the finish line and changes its state to AT_THE_FINNISH_LINE. Returns
     * whether it's the last Horse/Jockey pair to cross the finish line. If it's
     * the last Horse/Jockey pair crossing the finish line, wake up all the
     * Horse/Jockey pairs standing in the finish line.
     *
     * @return <code>true</code> if it's the last Horse/Jockey pair to cross the
     *         finish line; <code>false</code> otherwise
     */
    public synchronized boolean makeAMove()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());
        int horseJockeyId = hj.getHorseJockeyID();
        int raceId = hj.getRaceId();

        // if the horse is not beyond the finish
        if (racePosition[horseJockeyId] < distance[raceId])
        {
            int agility = ((HorseJockey) Thread.currentThread()).getAgility();
            int rand = ThreadLocalRandom.current().nextInt(1, agility + 1);

            // make a move
            racePosition[horseJockeyId] += rand;

            // iteration number for each horse
            iterationCounter[horseJockeyId]++;

            logger.setHorseJockeyMove(iterationCounter[horseJockeyId], racePosition[horseJockeyId],
                    horseJockeyId, raceId);

        }

        // if the horse is beyond the finish line and it hasn't been recorded yet
        if (racePosition[horseJockeyId] >= distance[raceId] && crossedFinish[horseJockeyId] == false)
        {
            // mark that it has crossed the finish line
            crossedFinish[horseJockeyId] = true;
            //  Change HorseJockey state to AT_THE_FINNISH_LINE
            hj.setHorseJockeyState(HorseJockeyState.AT_THE_FINNISH_LINE);
            logger.setHorseJockeyAtEnd(crossedFinish[horseJockeyId], horseJockeyId, raceId,
                    HorseJockeyState.AT_THE_FINNISH_LINE);

            // another Horse/Jockey pair has finished
            finishLineCount++;
        }

        // if all Horse/Jockey pairs have finished the race
        if (finishLineCount == N_numCompetitors)
        {
            // wake up the Horse/Jockey pairs at finish line
            raceEnded = true;
            notifyAll();

            // left finish line, reset flag
            crossedFinish[horseJockeyId] = false;
            logger.setHorseJockeyAtEnd(false, horseJockeyId, raceId);

            return true;
        } else
        {
            // wake up the next horse
            raceTurn[(horseJockeyId + 1) % SimulPar.N_numCompetitors] = true;
            notifyAll();
        }

        while (!raceTurn[horseJockeyId] && !raceEnded)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        raceTurn[horseJockeyId] = false;

        if (raceEnded)
        {
            // left finish line, reset flag
            crossedFinish[horseJockeyId] = false;
            logger.setHorseJockeyAtEnd(false, horseJockeyId, raceId);
        }

        return false;
    }

    /**
     * Checks whether the race has ended or not.
     *
     * @return <code>true</code> if the race has ended; <code>false</code>
     *         otherwise
     */
    public synchronized boolean hasRaceEnded()
    {
        return raceEnded;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Broker
    /**
     * Change the Broker state to SUPERVISING_THE_RACE. Wake up one of the
     * Horse/Jockey pairs from AT_THE_START_LINE.
     */
    public synchronized void startTheRace()
    {
        // Change Broker state to SUPERVISING_THE_RACE
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.SUPERVISING_THE_RACE);
        logger.setBrokerState(BrokerState.SUPERVISING_THE_RACE);

        // reset vars
        Arrays.fill(crossedFinish, false);
        finishLineCount = 0;
        raceEnded = false;

        raceTurn[0] = true;
        notifyAll();
    }

    /**
     * Checks which Horse/Jockey pairs have won the race.
     *
     * @return array indicating for each Horse/Jockey pair if they have won the
     *         race, indexed by their ID
     */
    public synchronized boolean[] reportResults()
    {
        boolean[] winners = new boolean[N_numCompetitors];
        int winningIteration = Integer.MAX_VALUE;
        int maxDistance = 0;

        // get the minimum iteration count
        for (int i = 0; i < N_numCompetitors; i++)
        {
            if (iterationCounter[i] <= winningIteration)
            {
                winningIteration = iterationCounter[i];
            }
        }

        // get the max value for the winning iteration
        for (int i = 0; i < N_numCompetitors; i++)
        {
            if (iterationCounter[i] == winningIteration && racePosition[i] > maxDistance)
            {
                maxDistance = racePosition[i];
            }
        }

        // check for multiple winners
        for (int i = 0; i < N_numCompetitors; i++)
        {
            if (iterationCounter[i] == winningIteration && racePosition[i] == maxDistance)
            {
                winners[i] = true;
            }
        }

        // reset vars
        Arrays.fill(iterationCounter, 0);
        Arrays.fill(racePosition, 0);

        return winners;
    }
}
