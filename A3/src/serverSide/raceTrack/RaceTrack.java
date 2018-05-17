package serverSide.raceTrack;

import states.BrokerState;
import states.HorseJockeyState;
import interfaces.IGeneralRepository;
import interfaces.IRaceTrack;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static settings.SimulPar.K_numRaces;
import static settings.SimulPar.N_numCompetitors;

/**
 * General description: Definition of the Race Track information sharing region.
 */
public class RaceTrack implements IRaceTrack {

    /**
     * Internal data
     */
    private final int[] distance;
    private final boolean[][] raceTurn = new boolean[K_numRaces][N_numCompetitors];
    private final boolean[][] crossedFinish = new boolean[K_numRaces][N_numCompetitors];
    private final int[][] racePosition = new int[K_numRaces][N_numCompetitors];
    private final int[][] iterationCounter = new int[K_numRaces][N_numCompetitors];
    private final boolean[] raceEnded = new boolean[K_numRaces];
    private final int[] finishLineCount = new int[K_numRaces];
    private final IGeneralRepository logger;
    private boolean shutdownServer = false;

    /**
     * Constructor
     *
     * @param logger General Repository of information, keeping a copy of the
     *               internal state of the problem
     */
    public RaceTrack(IGeneralRepository logger) throws RemoteException
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
     *
     * @param horseJockeyId the ID of the Horse/Jockey pair
     * @param raceId        the ID of the race
     */
    @Override
    public synchronized void proceedToStartLine(int horseJockeyId, int raceId) throws RemoteException
    {

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
     * @param horseJockeyId the ID of the Horse/Jockey pair
     * @param raceId        the ID of the race
     *
     * @param agility       the agility of the Horse/Jockey pair
     *
     * @return <code>true</code> if it's the last Horse/Jockey pair to cross the
     *         finish line; <code>false</code> otherwise
     */
    @Override
    public synchronized boolean makeAMove(int horseJockeyId, int raceId, int agility) throws RemoteException
    {

        // if the horse is not beyond the finish
        if (racePosition[raceId][horseJockeyId] < distance[raceId])
        {
            int rand = ThreadLocalRandom.current().nextInt(1, agility + 1);

            // make a move
            racePosition[raceId][horseJockeyId] += rand;

            // iteration number for each horse
            iterationCounter[raceId][horseJockeyId]++;

            logger.setHorseJockeyMove(iterationCounter[raceId][horseJockeyId], racePosition[raceId][horseJockeyId],
                    horseJockeyId, raceId);

            // Change Horse/Jockey state to RUNNING
            //TODO: change this
            //hj.setHorseJockeyState(HorseJockeyState.RUNNING);
            logger.setHorseJockeyState(HorseJockeyState.RUNNING, horseJockeyId, raceId);

        }

        // if the horse is beyond the finish line and it hasn't been recorded yet
        if (racePosition[raceId][horseJockeyId] >= distance[raceId] && crossedFinish[raceId][horseJockeyId] == false)
        {
            // mark that it has crossed the finish line
            crossedFinish[raceId][horseJockeyId] = true;
            //  Change HorseJockey state to AT_THE_FINNISH_LINE
            //TODO: change this
            //hj.setHorseJockeyState(HorseJockeyState.AT_THE_FINNISH_LINE);
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
        raceTurn[raceId][(horseJockeyId + 1) % N_numCompetitors] = true;
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
     * @param raceId the ID of the race
     *
     * @return <code>true</code> if the race has ended; <code>false</code>
     *         otherwise
     */
    @Override
    public synchronized boolean hasRaceEnded(int raceId)
    {
        return raceEnded[raceId];
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Broker
    /**
     * Change the Broker state to SUPERVISING_THE_RACE. Wake up one of the
     * Horse/Jockey pairs from AT_THE_START_LINE.
     *
     * @param raceId the ID of the race
     */
    @Override
    public synchronized void startTheRace(int raceId) throws RemoteException
    {
        // Change Broker state to SUPERVISING_THE_RACE
        logger.setBrokerState(BrokerState.SUPERVISING_THE_RACE);

        raceTurn[raceId][0] = true;
        notifyAll();
    }

    /**
     * Checks which Horse/Jockey pairs have won the race. Logs the standings of
     * the race.
     *
     * @param raceId the ID of the race
     *
     * @return array indicating for each Horse/Jockey pair if they have won the
     *         race, indexed by their ID
     */
    @Override
    public synchronized boolean[] reportResults(int raceId) throws RemoteException
    {

        boolean[] winners = new boolean[N_numCompetitors];
        boolean[] checked = new boolean[N_numCompetitors];
        int maxDistance = 0;
        int order[] = new int[N_numCompetitors];
        int stand = 1;
        boolean placed;

        Arrays.fill(checked, false);

        int[] iterC = iterationCounter[raceId].clone();
        Arrays.sort(iterC);

        // standings
        for (int j = 0; j < N_numCompetitors; j++)
        {
            placed = false;

            // get the max value for the iteration
            for (int i = 0; i < N_numCompetitors; i++)
            {
                if (iterationCounter[raceId][i] == iterC[j])
                {
                    if (checked[i] == false && racePosition[raceId][i] > maxDistance)
                    {
                        maxDistance = racePosition[raceId][i];
                    }
                }
            }

            // check for multiple winners
            for (int i = 0; i < N_numCompetitors; i++)
            {
                if (iterationCounter[raceId][i] == iterC[j] && racePosition[raceId][i] == maxDistance)
                {
                    checked[i] = true;
                    order[i] = stand;
                    placed = true;
                }
            }
            if (placed)
            {
                stand++;
            }
            maxDistance = 0;
        }

        // winners array
        for (int i = 0; i < N_numCompetitors; i++)
        {
            winners[i] = order[i] == 1;
        }

        logger.setHorseJockeyStands(order, raceId);

        return winners;
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
