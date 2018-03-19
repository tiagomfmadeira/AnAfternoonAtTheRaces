package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;
import entities.Spectator;
import entities.SpectatorState;
import genclass.GenericIO;
import main.SimulPar;

import java.util.Arrays;
import java.util.Random;

// TODO: interface to exclude access from undesired entities
public class RaceTrack
{
    private boolean raceStarted = false;
    private boolean finishedLineCrossed = false;
    private final int distance;
    private int[ ] racePosition = new int[SimulPar.N_numCompetitors];
    private boolean[ ] raceTurn = new boolean[SimulPar.N_numCompetitors];


    public RaceTrack(int distance)
    {
        //later this should be an array or something
        this.distance = distance;
    }

    //Horse Jockey
    public synchronized void proceedToStartLine()
    {
        //  Change HorseJockey state to AT_THE_PADDOCK
        ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE);
        GenericIO.writelnString(Thread.currentThread().getName() + " I'm at the Start line!");

        while (!raceStarted)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {

            }
        }
    }

    public synchronized void makeAMove()
    {
        // Change Horse/Jockey state to RUNNING
        ((HorseJockey)Thread.currentThread()).setHorseJockeyState(HorseJockeyState.RUNNING);

        int horseId = ((HorseJockey)Thread.currentThread()).getHorseJockeyID();
        int agility = ((HorseJockey)Thread.currentThread()).getAgility();

        Random rand = new Random();

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

        racePosition[horseId] += rand.nextInt(agility) + 1;

        if(racePosition[horseId] > distance)
        {
            finishedLineCrossed = true;
        }
        raceTurn[horseId] = false;
        raceTurn[(horseId + 1) % SimulPar.N_numCompetitors] = true;

        notifyAll();

    }

    public synchronized boolean hasFinishLineBeenCrossed()
    {
        return finishedLineCrossed;
    }


    //Broker
    public synchronized void startTheRace()
    {
        // Change Broker state to WAITING_FOR_BETS
        ((Broker)Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: Start the race!");

        raceStarted = true;
        finishedLineCrossed = false;
        Arrays.fill(racePosition, 0);
        Arrays.fill(raceTurn, false);
        raceTurn[0] = true;
        notifyAll();
    }
}
