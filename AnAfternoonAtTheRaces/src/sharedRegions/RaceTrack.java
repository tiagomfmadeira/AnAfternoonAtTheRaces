package sharedRegions;

import entities.HorseJockey;
import entities.HorseJockeyState;
import entities.Spectator;
import entities.SpectatorState;
import main.SimulPar;

import java.util.Arrays;
import java.util.Random;

// TODO: interface to exclude access from undesired entities
public class RaceTrack {

    private boolean raceStarted = false;
    private boolean finishedLineCrossed = false;
    private int distance;
    private int[] racePosition = new int[SimulPar.N_numCompetitors];
    private boolean[] raceTurn = new boolean[SimulPar.N_numCompetitors];


    public RaceTrack(int distance){
        //later this should be an array or something
        this.distance = distance;
    }


    //Horse Jockey
    public synchronized void proceedToStartLine(){

        while (!raceStarted) {
            try {
                wait();
            }
            catch (InterruptedException e) {}
        }

        ((HorseJockey)Thread.currentThread()).setHorseJockeyState(HorseJockeyState.RUNNING);

    }

    public synchronized void makeAMove(){

        int horseId = ((HorseJockey)Thread.currentThread()).getHJID();
        int agility = ((HorseJockey)Thread.currentThread()).getAgility();

        Random rand = new Random();

        while(!raceTurn[horseId]) {
            try {
                wait ();
            }
            catch (InterruptedException e) {}
        }

        racePosition[horseId] += rand.nextInt(agility) + 1;

        if(racePosition[horseId] > distance)
            finishedLineCrossed = true;

        raceTurn[horseId] = false;
        raceTurn[horseId + 1 % SimulPar.N_numCompetitors] = true;

        notifyAll();

    }

    public synchronized boolean hasFinishLineBeenCrossed(){
        return finishedLineCrossed;
    }


    //Broker
    public synchronized void startTheRace(){
        raceStarted = true;
        finishedLineCrossed = false;
        Arrays.fill(racePosition, 0);
        Arrays.fill(raceTurn, false);
        raceTurn[0] = true;
        notifyAll();
    }
    
}
