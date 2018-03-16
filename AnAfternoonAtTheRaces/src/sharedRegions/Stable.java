package sharedRegions;

import entities.Broker;
import entities.HorseJockey;
import entities.HorseJockeyState;



public class Stable {

    //here for now, later in main

    // prob one for each one
    private boolean[] proceedToStableFlag;
    private int[] proceededHorsesCount;
    private final int K_numRaces;
    private final int N_numCompetitors;


    // boolean bidimension array that indicates if a specific horse can advance
    public Stable(int K_numRaces, int N_numCompetitors){
        this.K_numRaces = K_numRaces;
        this.N_numCompetitors = N_numCompetitors;
        this.proceedToStableFlag = new boolean[K_numRaces];
    }



    public synchronized boolean proceedToStable() {

        boolean isLastHorse = false;

        int raceId = ((HorseJockey) Thread.currentThread()).getRaceId();

        while (!proceedToStableFlag[raceId]) {
            try {
                wait ();
            }
            catch (InterruptedException e) {}
        }

        proceededHorsesCount[raceId]++;

        if(proceededHorsesCount[raceId] == N_numCompetitors){
            isLastHorse = true;
        }

        ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK);

        return isLastHorse;

    }

    public synchronized void summonHorsesToPaddock(int k) {

        proceedToStableFlag[k] = true;
        notifyAll();

    }





}
