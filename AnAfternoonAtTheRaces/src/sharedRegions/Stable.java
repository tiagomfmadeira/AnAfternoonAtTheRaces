package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;
import genclass.GenericIO;
import main.SimulPar;
import static main.SimulPar.N_numCompetitors;


public class Stable
{

    //here for now, later in main

    // prob one for each one
    private boolean[ ] proceedToPaddockFlag;
    private int[ ] proceededHorsesCount;
    private boolean nextRaceExists = true;


    // boolean bidimension array that indicates if a specific horse can advance
    public Stable()
    {
        this.proceedToPaddockFlag = new boolean[SimulPar.K_numRaces];
        this.proceededHorsesCount = new int[SimulPar.K_numRaces];
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Broker

    public synchronized void summonHorsesToPaddock(int raceID)
    {
        GenericIO.writelnString(Thread.currentThread().getName() +  " says: Bring the horses for race " + raceID +"!");

        proceedToPaddockFlag[raceID] = true;
        notifyAll();
    }

    public synchronized void entertainTheGuests()
    {
        nextRaceExists = false;
        notifyAll();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Horse/Jockey

    public synchronized void proceedToStable()
    {
        //  Change HorseJockey state to AT_THE_STABLE
        ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_STABLE);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm at the Stable!");

        // Get race ID
        int raceID = ((HorseJockey) Thread.currentThread()).getRaceId();

        // Check the flag for this race
        while (!proceedToPaddockFlag[raceID] && nextRaceExists)
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }

        proceededHorsesCount[raceID]++;

        if (proceededHorsesCount[raceID] ==N_numCompetitors)        // last horse to leave stable
        {
            // reset var for next run
            proceedToPaddockFlag[raceID] = false;
        }
    }
}
