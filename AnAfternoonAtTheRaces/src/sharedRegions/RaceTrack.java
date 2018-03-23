package sharedRegions;

import entities.HorseJockey;
import entities.HorseJockeyState;
import genclass.GenericIO;
import main.SimulPar;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Random;

import static main.SimulPar.N_numCompetitors;

// TODO: interface to exclude access from undesired entities
public class RaceTrack
{
    private final int distance;
    private int[ ] racePosition = new int[SimulPar.N_numCompetitors];

    private boolean[ ] raceTurn = new boolean[SimulPar.N_numCompetitors];
    private boolean[ ] crossedFinish = new boolean[SimulPar.N_numCompetitors];
    private boolean[ ] winners = new boolean[SimulPar.N_numCompetitors];

    private boolean lastArrived = false,
                                     winnersChosen = false;

    private int horseMoveCounter = 0,
                          finishLineCount = 0,
                          helperHorse = 0;
    private int[] iterationCounter = new int[SimulPar.N_numCompetitors];

    private Logger logger;

    public RaceTrack(int distance, Logger logger)
    {
        //later this should be an array or something
        this.distance = distance;
        this.logger = logger;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Horse/Jockey

    public synchronized void proceedToStartLine()
    {
        //  Change HorseJockey state to AT_THE_START_LINE
        ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE);
        logger.setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE,
                ((HorseJockey) Thread.currentThread()).getHorseJockeyID());

        int horseId = ((HorseJockey)Thread.currentThread()).getHorseJockeyID();

        while (!raceTurn[horseId])
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {

            }
        }
        raceTurn[horseId] = false;

        // Change Horse/Jockey state to RUNNING
        ((HorseJockey)Thread.currentThread()).setHorseJockeyState(HorseJockeyState.RUNNING);
        logger.setHorseJockeyState(HorseJockeyState.RUNNING,
                ((HorseJockey) Thread.currentThread()).getHorseJockeyID());
    }

    public synchronized boolean makeAMove()
    {
       boolean lastToCross = false;

        int horseId = ((HorseJockey)Thread.currentThread()).getHorseJockeyID();

        if(racePosition[horseId]<distance)
        {
            int agility = ((HorseJockey)Thread.currentThread()).getAgility();
            Random rand = new Random();
            // make a move
            racePosition[horseId] += rand.nextInt(agility) + 1;
        }

        // number of horses awaken so far this iteration
        horseMoveCounter++;
        iterationCounter[horseId]++;

        // wake up the next horse
        raceTurn[(horseId + 1) % SimulPar.N_numCompetitors] = true;
        notifyAll();

        while(!raceTurn[horseId])
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }
        raceTurn[horseId] = false;

        // if the horse is beyond the finish line and it hasn't been recorded yet
        if(racePosition[horseId]>=distance && crossedFinish[horseId] == false)
        {
            // mark that it has crossed the finish line
            crossedFinish[horseId] = true;
            //  Change HorseJockey state to AT_THE_FINNISH_LINE
            ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_FINNISH_LINE);
            logger.setHorseJockeyState(HorseJockeyState.AT_THE_FINNISH_LINE,
                    ((HorseJockey) Thread.currentThread()).getHorseJockeyID());

            // another horse has finished
            finishLineCount++;
        }

        // If all horses woke up, this iteration is done
        if ( horseMoveCounter == N_numCompetitors)
        {
            horseMoveCounter = 0;

            // if someone won and the winner hasn't been recorded yet
            if(finishLineCount > 0 && !winnersChosen)
            {
                // record the result
                System.arraycopy(crossedFinish, 0, winners, 0, N_numCompetitors) ;
                winnersChosen = true;
            }
        }

        // if all the horses have finished the race
        if(finishLineCount==N_numCompetitors && !lastArrived)
        {

            lastToCross = true;
            lastArrived = true;
        }

        // all the horses have crossed the finish line
        if(lastArrived == true)
        {
            // wake up the next horse
            raceTurn[(horseId + 1) % SimulPar.N_numCompetitors] = true;
            notifyAll();
            helperHorse++;

            // if is the last horse to be released from the race track
            if(helperHorse==N_numCompetitors)
            {
                // debug print
                for (int i = 0; i < N_numCompetitors; i++) {
                    //GenericIO.writelnString("Horse " + i + " is winner = " + winners[i]);
                }
                // reset the vars
                Arrays.fill(racePosition, 0);
                Arrays.fill(crossedFinish, false);
                Arrays.fill(raceTurn, false);
                horseMoveCounter = 0;
                helperHorse = 0;
                lastArrived = false;
                winnersChosen=false;
                Arrays.fill(iterationCounter,0);
            }
        }
        return lastToCross;
    }

    public synchronized boolean hasRaceEnded()
    {
        return finishLineCount==N_numCompetitors;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Broker

    public synchronized void startTheRace()
    {
        finishLineCount = 0;
        Arrays.fill(winners, false);
        raceTurn[0] = true;
        notifyAll();

    }

    public synchronized HashSet reportResults()
    {
        HashSet declaredWinners = new HashSet();
        int tmpWinner = 0;
        int maxPosition = 0;

        // for each competitor
        for (int i = 0; i < N_numCompetitors; i++)
        {
            if (winners[ i ] == true && racePosition[ i ] > maxPosition)
            {
                // save the id of HorseJockey
                tmpWinner = i ;
                // save the max position encountered
                maxPosition = racePosition[ i ] ;
            }
        }
        // found a winner
        declaredWinners.add(tmpWinner);

        // check for multiple winners
        for (int i = 0; i < N_numCompetitors; i++)
        {
            if (winners[ i ] == true && racePosition[ i ] == maxPosition)
            {
                declaredWinners.add( i );
            }
        }
        return declaredWinners ;
    }

}