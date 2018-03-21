package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;
import entities.Spectator;
import entities.SpectatorState;
import genclass.GenericIO;
import main.SimulPar;

import static main.SimulPar.K_numRaces;
import static main.SimulPar.N_numCompetitors;
import static main.SimulPar.M_numSpectators;

import java.util.Arrays;
import java.util.Random;

// TODO: interface to exclude access from undesired entities
public class RaceTrack
{
    private final int distance;
    private int[ ] racePosition = new int[SimulPar.N_numCompetitors];

    private boolean[ ] raceTurn = new boolean[SimulPar.N_numCompetitors];
    private boolean[ ] crossedFinish = new boolean[SimulPar.N_numCompetitors];
    private boolean[ ] winners = new boolean[SimulPar.N_numCompetitors];

    private boolean lastArrived = false;
    private boolean winnersChosen = false;

    private int horseMoveCounter = 0;
    private int finishLineCount = 0;
    private int helperHorse = 0;

    public RaceTrack(int distance)
    {
        //later this should be an array or something
        this.distance = distance;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Horse/Jockey

    public synchronized void proceedToStartLine()
    {
        //  Change HorseJockey state to AT_THE_START_LINE
        ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE);
        GenericIO.writelnString(Thread.currentThread().getName() + " I'm at the Start line!");

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

        // number of horses  so far this iteration
        horseMoveCounter++;

        //GenericIO.writelnString(Thread.currentThread().getName() + " Gonna wake up horse  " + (horseId + 1) % SimulPar.N_numCompetitors  );
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
            GenericIO.writelnString(Thread.currentThread().getName() + " I have crossed the finish line!");

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
            GenericIO.writelnString(Thread.currentThread().getName() + " I was the last to cross the finish line!");

            lastToCross = true;
            lastArrived = true;
        }

        if(lastArrived == true)
        {
            //GenericIO.writelnString(Thread.currentThread().getName() + " Gonna wake up horse  " + (horseId + 1) % SimulPar.N_numCompetitors  );

            //  Change HorseJockey state to AT_THE_FINNISH_LINE
            ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_FINNISH_LINE);

            // wake up the next horse
            raceTurn[(horseId + 1) % SimulPar.N_numCompetitors] = true;
            notifyAll();
            helperHorse++;

            if(helperHorse==N_numCompetitors)
            {
                //debug print
                for (int i = 0; i < N_numCompetitors; i++) {
                    GenericIO.writelnString("Horse " + i + " is winner = " + winners[i]);
                }
                //reset the vars
                Arrays.fill(racePosition, 0);
                Arrays.fill(crossedFinish, false);
                Arrays.fill(raceTurn, false);
                horseMoveCounter = 0;
                helperHorse = 0;
                lastArrived = false;
                winnersChosen=false;
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

        GenericIO.writelnString(Thread.currentThread().getName() + " says: The race can begin!");
    }
}