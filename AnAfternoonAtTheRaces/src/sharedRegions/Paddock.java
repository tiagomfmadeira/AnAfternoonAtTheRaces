package sharedRegions;

import entities.*;
import genclass.GenericIO;
import main.SimulPar;

import static main.SimulPar.N_numCompetitors;
import static main.SimulPar.M_numSpectators;

// TODO: interface to exclude access from undesired entities
public class Paddock
{

    HorseJockey[ ] horses = new HorseJockey[N_numCompetitors];

    private boolean lastSpectatorGoCheckHorses = false;
    private boolean lastHorseProceedToStartLine = false;
    private int horsesAtPaddockCount = 0;
    private int horsesProceededToStartLineCount = 0;
    private int spectatorsAtPaddockCount = 0;
    private int spectatorsFinishedAppraising = 0;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //HorseJockey

    public synchronized boolean proceedToPaddock()
    {
        boolean isLastHorse = false;

        //  Change HorseJockey state to AT_THE_PADDOCK
        ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm at the Paddock!");

        horsesAtPaddockCount++;

        // Get ID of the horse/Jockey
        int horseJockeyID = ((HorseJockey) Thread.currentThread()).getHorseJockeyID();

        // Save reference of the Horse/Jockey to be used by spectator thread in appraising
        horses[horseJockeyID] = (HorseJockey) Thread.currentThread();

         if(horsesAtPaddockCount == SimulPar.N_numCompetitors)
        {
            GenericIO.writelnString(Thread.currentThread().getName() + " says: I am the last horse to arrive at the Paddock!");
            isLastHorse = true;
            horsesAtPaddockCount = 0;
        }

        return isLastHorse;
    }

    public synchronized void sleepAtThePaddock()
    {
        // Wait for all the spectators to check the horses
        while (!lastSpectatorGoCheckHorses)
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }
    }

    public synchronized void proceedToStartLine()
    {
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm headed to the start line!");

        horsesProceededToStartLineCount++;

        if(horsesProceededToStartLineCount == N_numCompetitors)
        {
            GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm the last horse to leave for the start line!");
            lastHorseProceedToStartLine = true;
            notifyAll();

            // reset vars for next run
            horsesProceededToStartLineCount = 0;
            // next time horses come to paddock they block
            lastSpectatorGoCheckHorses = false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Spectator

    public synchronized boolean goCheckHorses()
    {
         boolean isLastSpectator = false;

        //  Change Spectator state to APPRAISING_THE_HORSES
        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.APPRAISING_THE_HORSES);

        spectatorsAtPaddockCount++;

        if(spectatorsAtPaddockCount == SimulPar.M_numSpectators)
        {
            GenericIO.writelnString(Thread.currentThread().getName() +  " says: I'm the last Spectator to reach the Paddock");
            isLastSpectator = true;
            spectatorsAtPaddockCount = 0;
            notifyAll();
        }
        return isLastSpectator;
    }

    public synchronized int appraisingHorses()
    {
        while (!lastHorseProceedToStartLine)
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }

        spectatorsFinishedAppraising++;

        if(spectatorsFinishedAppraising == M_numSpectators)
        {
            spectatorsFinishedAppraising = 0;
            lastHorseProceedToStartLine = false;
        }

        int horseID = 0;

        // Decide which horse to bet on, TODO further logic should be implemented
        for (int i = 0; i < N_numCompetitors; i++) {
            if(horses[ i ].getAgility() > horses[ horseID].getAgility())
            {
                horseID = i;
            }
        }
        return horseID;
    }
    public synchronized void lastToCheckHorses()
    {
        GenericIO.writelnString(Thread.currentThread().getName() +  " says: The horses can leave to the start line!");
        lastSpectatorGoCheckHorses = true;
        notifyAll();
    }
}
