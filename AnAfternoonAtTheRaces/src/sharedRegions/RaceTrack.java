package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;
import main.SimulPar;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import static main.SimulPar.N_numCompetitors;

public class RaceTrack
{

    private final int distance;
    private final boolean[] raceTurn = new boolean[SimulPar.N_numCompetitors];
    private final boolean[] crossedFinish = new boolean[SimulPar.N_numCompetitors];
    private final int[] racePosition = new int[SimulPar.N_numCompetitors];
    private final int[] iterationCounter = new int[SimulPar.N_numCompetitors];
    private boolean raceEnded = true;
    private int finishLineCount = 0;
    private final Logger logger;

    public RaceTrack(int distance, Logger logger)
    {
        //later this should be an array or something
        this.distance = distance;
        this.logger = logger;
        this.logger.setDistanceInRace(distance);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Horse/Jockey
    public synchronized void proceedToStartLine()
    {
        //  Change HorseJockey state to AT_THE_START_LINE
        HorseJockey hj = (HorseJockey) Thread.currentThread();
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE);
        int horseId = hj.getHorseJockeyID();
        int raceId = hj.getRaceId();

        logger.setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE,
                horseId, raceId);

        while (!raceTurn[horseId])
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        raceTurn[horseId] = false;

        // Change Horse/Jockey state to RUNNING
        hj.setHorseJockeyState(HorseJockeyState.RUNNING);
        logger.setHorseJockeyState(HorseJockeyState.RUNNING, horseId, raceId);
    }

    public synchronized boolean makeAMove()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());
        int horseId = hj.getHorseJockeyID();
        int raceId = hj.getRaceId();

        // if the horse is not beyond the finish
        if (racePosition[horseId] < distance)
        {
            int agility = ((HorseJockey) Thread.currentThread()).getAgility();
            int rand = ThreadLocalRandom.current().nextInt(1, agility + 1);

            // make a move
            racePosition[horseId] += rand;

            // iteration number for each horse
            iterationCounter[horseId]++;

            logger.setHorseMove(iterationCounter[horseId], racePosition[horseId], horseId, raceId);

        }

        // if the horse is beyond the finish line and it hasn't been recorded yet
        if (racePosition[horseId] >= distance && crossedFinish[horseId] == false)
        {
            // mark that it has crossed the finish line
            crossedFinish[horseId] = true;
            //  Change HorseJockey state to AT_THE_FINNISH_LINE
            hj.setHorseJockeyState(HorseJockeyState.AT_THE_FINNISH_LINE);
            logger.setHorseAtEnd(crossedFinish[horseId], horseId, raceId, HorseJockeyState.AT_THE_FINNISH_LINE);

            // another horse has finished
            finishLineCount++;
        }

        // if all the horses have finished the race
        if (finishLineCount == N_numCompetitors)
        {
            // wake up the horses at finish line
            raceEnded = true;
            notifyAll();

            return true;
        }

        // wake up the next horse
        raceTurn[(horseId + 1) % SimulPar.N_numCompetitors] = true;
        notifyAll();

        while (!raceTurn[horseId] && !raceEnded)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        raceTurn[horseId] = false;

        return false;
    }

    public synchronized boolean hasRaceEnded()
    {
        return raceEnded;
    }

    public synchronized void proceedToStableFinal()
    {
        int horseId = ((HorseJockey) Thread.currentThread()).getHorseJockeyID();
        int raceId = ((HorseJockey) Thread.currentThread()).getRaceId();
        // reset vars
        crossedFinish[horseId] = false;
        logger.setHorseAtEnd(false, horseId, raceId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Broker
    public synchronized void startTheRace()
    {
        // Change Broker state to SUPERVISING_THE_RACE
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.SUPERVISING_THE_RACE);
        logger.setBrokerState(BrokerState.SUPERVISING_THE_RACE);

        Arrays.fill(crossedFinish, false);
        finishLineCount = 0;
        raceEnded = false;
        raceTurn[0] = true;
        notifyAll();
    }

    public synchronized boolean[] reportResults()
    {
        boolean[] winners = new boolean[N_numCompetitors];
        int winningIteration = Integer.MAX_VALUE;
        int maxDistance = 0;

        //  get the minimum iteration count
        for (int i = 0; i < N_numCompetitors; i++)
        {
            if (iterationCounter[i] <= winningIteration)
            {
                winningIteration = iterationCounter[i];
            }
        }

        //  get the max value for the winning iteration
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

        // reset
        Arrays.fill(iterationCounter, 0);
        Arrays.fill(racePosition, 0);

        return winners;
    }

}
