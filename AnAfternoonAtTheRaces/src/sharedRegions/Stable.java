package sharedRegions;

import entities.HorseJockey;
import genclass.GenericIO;
import main.SimulPar;


public class Stable
{

    //here for now, later in main

    // prob one for each one
    private boolean[ ] proceedToPaddockFlag;
    private int[ ] proceededHorsesCount;


    // boolean bidimension array that indicates if a specific horse can advance
    public Stable()
    {
        this.proceedToPaddockFlag = new boolean[SimulPar.K_numRaces];
        this.proceededHorsesCount = new int[SimulPar.K_numRaces];
    }


    // Broker
    public synchronized void summonHorsesToPaddock(int raceID)
    {
        GenericIO.writelnString(Thread.currentThread().getName() +  " says: Bring the horses for race " + raceID +"!");

        proceedToPaddockFlag[raceID] = true;
        notifyAll();
    }

    // Horse/Jockey
    public synchronized boolean proceedToStable()
    {
        boolean isLastHorse = false;

        // Get race ID
        int raceID = ((HorseJockey) Thread.currentThread()).getRaceId();

        // Check the flag for this race
        while (!proceedToPaddockFlag[raceID])
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }

        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm headed to the paddock!");

        proceededHorsesCount[raceID]++;

        if(proceededHorsesCount[raceID] == SimulPar.N_numCompetitors)
        {
            GenericIO.writelnString(Thread.currentThread().getName() + " says: I am the last horse leaving for the Paddock!");
            isLastHorse = true;
            proceedToPaddockFlag[raceID] = false;
        }
        return isLastHorse;
    }
}
