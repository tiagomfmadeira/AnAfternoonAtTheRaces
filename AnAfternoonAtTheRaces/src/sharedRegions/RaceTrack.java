package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;
import main.SimulPar;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static main.SimulPar.K_numRaces;
import static main.SimulPar.N_numCompetitors;

/**
 * General description: Definition of the Race Track information sharing region.
 */
public class RaceTrack implements SharedRegion
{

    /**
     * Internal data
     */
    private final int[] distance;
    private final boolean[][] raceTurn = new boolean[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private final boolean[][] crossedFinish = new boolean[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private final int[][] racePosition = new int[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private final int[][] iterationCounter = new int[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private final boolean[] raceEnded = new boolean[SimulPar.K_numRaces];
    private final int[] finishLineCount = new int[SimulPar.K_numRaces];
    private final GeneralRepository logger;

    /**
     * Constructor
     *
     * @param logger   General Repository of information, keeping a copy of the
     *                 internal state of the problem
     */
    public RaceTrack(GeneralRepository logger)
    {
        //Distance now initialized here
        int[] distance = new int[K_numRaces];
        Random rand = new Random();
        for (int i = 0; i < K_numRaces; i++)
        {
            distance[i] = 15 + rand.nextInt(10);
        }

        //later this should be an array or something
        this.distance = distance;
        this.logger = logger;
        this.logger.setDistanceInRace(distance);

        for (boolean[] row : raceTurn)
        {
            Arrays.fill(row, false);
        }

        for (boolean[] row : crossedFinish)
        {
            Arrays.fill(row, false);
        }

        for (int[] row : racePosition)
        {
            Arrays.fill(row, 0);
        }

        for (int[] row : iterationCounter)
        {
            Arrays.fill(row, 0);
        }

        Arrays.fill(raceEnded, false);

        Arrays.fill(finishLineCount, 0);
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

        while (!raceTurn[raceId][horseJockeyId])
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        raceTurn[raceId][horseJockeyId] = false;
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
        if (racePosition[raceId][horseJockeyId] < distance[raceId])
        {
            int agility = ((HorseJockey) Thread.currentThread()).getAgility();
            int rand = ThreadLocalRandom.current().nextInt(1, agility + 1);

            // make a move
            racePosition[raceId][horseJockeyId] += rand;

            // iteration number for each horse
            iterationCounter[raceId][horseJockeyId]++;

            logger.setHorseJockeyMove(iterationCounter[raceId][horseJockeyId], racePosition[raceId][horseJockeyId],
                    horseJockeyId, raceId);

            // Change Horse/Jockey state to RUNNING
            hj.setHorseJockeyState(HorseJockeyState.RUNNING);
            logger.setHorseJockeyState(HorseJockeyState.RUNNING, horseJockeyId, raceId);

        }

        // if the horse is beyond the finish line and it hasn't been recorded yet
        if (racePosition[raceId][horseJockeyId] >= distance[raceId] && crossedFinish[raceId][horseJockeyId] == false)
        {
            // mark that it has crossed the finish line
            crossedFinish[raceId][horseJockeyId] = true;
            //  Change HorseJockey state to AT_THE_FINNISH_LINE
            hj.setHorseJockeyState(HorseJockeyState.AT_THE_FINNISH_LINE);
            logger.setHorseJockeyState(HorseJockeyState.AT_THE_FINNISH_LINE, horseJockeyId, raceId);

            // another Horse/Jockey pair has finished
            finishLineCount[raceId]++;
        }

        // if all Horse/Jockey pairs have finished the race
        if (finishLineCount[raceId] == N_numCompetitors)
        {
            // wake up the Horse/Jockey pairs at finish line
            raceEnded[raceId] = true;
            notifyAll();

            return true;
        }

        // wake up the next horse
        raceTurn[raceId][(horseJockeyId + 1) % SimulPar.N_numCompetitors] = true;
        notifyAll();

        while (!raceTurn[raceId][horseJockeyId] && !raceEnded[raceId])
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        raceTurn[raceId][horseJockeyId] = false;

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
        HorseJockey hj = ((HorseJockey) Thread.currentThread());
        int raceId = hj.getRaceId();

        return raceEnded[raceId];
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
        Broker broker = (Broker) Thread.currentThread();
        broker.setBrokerState(BrokerState.SUPERVISING_THE_RACE);
        logger.setBrokerState(BrokerState.SUPERVISING_THE_RACE);

        int raceId = broker.getCurrentRace();

        raceTurn[raceId][0] = true;
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
        int raceId = ((Broker) Thread.currentThread()).getCurrentRace();
        boolean[] winners = new boolean[N_numCompetitors];
        int winningIteration = Integer.MAX_VALUE;
        int maxDistance = 0;
        int order[] = new int[N_numCompetitors];

        Arrays.fill(winners, false);

        // get the minimum iteration count
        for (int i = 0; i < N_numCompetitors; i++)
        {
            if (iterationCounter[raceId][i] <= winningIteration)
            {
                winningIteration = iterationCounter[raceId][i];
            }
        }

        // get the max value for the winning iteration
        for (int i = 0; i < N_numCompetitors; i++)
        {
            if (iterationCounter[raceId][i] == winningIteration && racePosition[raceId][i] > maxDistance)
            {
                maxDistance = racePosition[raceId][i];
            }
        }

        // check for multiple winners
        for (int i = 0; i < N_numCompetitors; i++)
        {
            if (iterationCounter[raceId][i] == winningIteration && racePosition[raceId][i] == maxDistance)
            {
                winners[i] = true;
                order[i] = 1;
            }
        }

        int[] iterC = iterationCounter[raceId].clone();
        int place = 1;
        Arrays.sort(iterC);

        // calculate the standings
        for (int i = 0; i < N_numCompetitors; i++)
        {
            for (int j = 0; j < N_numCompetitors; j++)
            {

                if (winners[i] != true)
                {
                    if (iterationCounter[raceId][i] == iterC[j])
                    {
                        order[i] = ++place;
                        break;
                    }
                }

            }
        }
        logger.setHorseJockeyStands(order, raceId);

        return winners;
    }

}
