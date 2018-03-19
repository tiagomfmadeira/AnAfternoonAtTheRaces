package sharedRegions;

import entities.*;
import genclass.GenericIO;

import static main.SimulPar.N_numCompetitors;

// TODO: interface to exclude access from undesired entities
public class Paddock
{

    HorseJockey[ ] horses = new HorseJockey[N_numCompetitors];

    // Check when turns to false again
    private boolean lastSpectatorGoCheckHorses = false;
    private boolean lastHorseProceedToStartLine = false;
    private int proceededHorsesCount = 0;

    //HorseJockey
    public synchronized void proceedToPaddock()
    {
        //  Change HorseJockey state to AT_THE_PADDOCK
        ((HorseJockey) Thread.currentThread()).setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm at the Paddock!");

        // Get ID of the horse/Jockey
        int horseJockeyID = ((HorseJockey) Thread.currentThread()).getHorseJockeyID();

        // Save reference of the Horse/Jockey to be used by spectator thread in appraising
        horses[horseJockeyID] = (HorseJockey) Thread.currentThread();

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

        proceededHorsesCount++;

        if(proceededHorsesCount == N_numCompetitors)
        {
           GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm the last horse at the start line!");
            proceededHorsesCount = 0;
            lastHorseProceedToStartLine = true;
             notifyAll();
        }
    }

    //Spectator

    public synchronized int goCheckHorses()
    {
        //  Change Spectator state to APPRAISING_THE_HORSES
        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.APPRAISING_THE_HORSES);

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
        GenericIO.writelnString(Thread.currentThread().getName() +  " says: Everyone has appraised the horses!");
        lastSpectatorGoCheckHorses = true;
        notifyAll();
    }
}
