package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;
import main.SimulPar;


public class Stable {

    //here for now, later in main

    // prob one for each one
    private boolean[] proceedToPaddockFlag;
    private int[] proceededHorsesCount;


    // boolean bidimension array that indicates if a specific horse can advance
    public Stable(){
        this.proceedToPaddockFlag = new boolean[SimulPar.K_numRaces];
        this.proceededHorsesCount = new int[SimulPar.K_numRaces];
    }


    // HorseJockey
    public synchronized boolean proceedToStable() {

        boolean isLastHorse = false;

        int raceId = ((HorseJockey) Thread.currentThread()).getRaceId();


        while (!proceedToPaddockFlag[raceId]) {
            try {
                wait ();
            }
            catch (InterruptedException e) {}
        }

        proceededHorsesCount[raceId]++;

        if(proceededHorsesCount[raceId] == SimulPar.N_numCompetitors){
            isLastHorse = true;
            proceedToPaddockFlag[raceId] = false;
        }

        ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK);

        return isLastHorse;

    }

    // Broker
    public synchronized void summonHorsesToPaddock(int k) {

        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);

        proceedToPaddockFlag[k] = true;
        notifyAll();


    }





}
