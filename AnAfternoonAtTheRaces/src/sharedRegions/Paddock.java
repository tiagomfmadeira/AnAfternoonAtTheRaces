package sharedRegions;

import entities.*;
import genclass.GenericIO;
import static main.SimulPar.M_numSpectators;
import static main.SimulPar.N_numCompetitors;

public class Paddock
{
    HorseJockey[ ] horses = new HorseJockey[N_numCompetitors];

    private boolean lastSpectatorGoCheckHorses = false,
                                     lastHorseProceedToStartLine = false;
    private int horsesAtPaddockCount = 0,
                          spectatorsAtPaddockCount = 0;

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

         if(horsesAtPaddockCount == N_numCompetitors)
        {
            GenericIO.writelnString(Thread.currentThread().getName() + " says: I am the last horse to arrive at the Paddock!");
            isLastHorse = true;
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

        horsesAtPaddockCount--;

        // if is last horse to leave paddock
        if(horsesAtPaddockCount == 0)
        {
            GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm the last horse to leave for the start line!");
            lastHorseProceedToStartLine = true;
            notifyAll();

            // reset var, so next time horses come to paddock they block
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

        if(spectatorsAtPaddockCount == M_numSpectators)
        {
            GenericIO.writelnString(Thread.currentThread().getName() +  " says: I'm the last Spectator to reach the Paddock");
            // return var
            isLastSpectator = true;
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

        spectatorsAtPaddockCount--;

        // if is last spectator to leave
        if(spectatorsAtPaddockCount == 0)
        {
            // reset , so spectators will block next race
            lastHorseProceedToStartLine = false;
        }

        int horseID = 0;
        //TODO: further logic should be implemented
        // Decide which horse to bet on
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Broker


    public synchronized double[ ] acceptTheBets()
    {
        double [ ] odds = new double [N_numCompetitors];
        double totalAgility = 0;

        // get the total agility
        for (int i = 0; i < N_numCompetitors; i++) {
           totalAgility += horses[ i ].getAgility();
        }
        GenericIO.writelnString("Total Agility: " + totalAgility);

        // calculate the odds of each horse
        for (int i = 0; i < N_numCompetitors; i++) {
           odds[ i ]= horses[ i ].getAgility()/totalAgility;
                   GenericIO.writelnString("Agility: " +  horses[ i ].getAgility());
        }

        // debug print
        for (int i = 0; i < N_numCompetitors; i++) {
            GenericIO.writelnString("Horse " + i + " " + odds[i]);
        }
        return odds;
    }
}