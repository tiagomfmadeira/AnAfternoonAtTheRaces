package sharedRegions;

// TODO: interface to exclude access from undesired entities

import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.Spectator;
import entities.SpectatorState;
import genclass.GenericIO;

import static main.SimulPar.K_numRaces;
import static main.SimulPar.N_numCompetitors;
import static main.SimulPar.M_numSpectators;

public class BettingCenter {
    
    // saves the SpectatorID, HorseJockeyID and Value of bet for each Spectator
    int[ ][ ] bets = new int [M_numSpectators ] [3];

    private boolean nextSpectatorCanPlaceBet= false;
    private boolean lastSpectatorToPlaceBet = false;
    private boolean canPlaceBet = true;

    private int numBets = 0;

    // Broker
        public synchronized void acceptTheBets()
    {
        // Change Broker state to WAITING_FOR_BETS
        ((Broker)Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm waiting for bets!");

        while (!lastSpectatorToPlaceBet)
        {
            try
            {
                while (!nextSpectatorCanPlaceBet)
                {
                    try
                    {
                        wait();
                    }
                    catch (InterruptedException e)
                    {
                        throw e;
                    }
                }

                    nextSpectatorCanPlaceBet = false;

                    GenericIO.writelnString(Thread.currentThread().getName() + " says: I will now accept a bet!");

                    canPlaceBet = true;
                    notifyAll();
            }
            catch (InterruptedException e)
            {

            }
        }
    }

    // Spectator
    public synchronized boolean placeABet(int horseID)
    {
        while (!canPlaceBet)
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }

        canPlaceBet = false;

        boolean isLastSpectator = false;

        //  Change Spectator state to PLACING_A_BET
        ((Spectator)Thread.currentThread()).setSpectatorState(SpectatorState.PLACING_A_BET);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm placing a bet!");

        // Saved information about the bet, maybe do this in broker thread??
        bets[numBets][0] =((Spectator)Thread.currentThread()).getSpectatorID();
        bets[numBets][1] = horseID;
        // TODO improve logic of bet value
        bets[numBets][2] = ((Spectator)Thread.currentThread()).getWalletValue()/4;

        numBets++;

        if(numBets == N_numCompetitors)
        {
           GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm the last spectator to bet!");
            isLastSpectator = true;
            numBets = 0;
            lastSpectatorToPlaceBet = true;
            notifyAll();
        }
        else
        {
            nextSpectatorCanPlaceBet = true;
            notifyAll();
        }

        return isLastSpectator;
    }

    public synchronized void lastToPlaceBet()
    {
        GenericIO.writelnString("Betting information stored in Betting center: ");
        for (int i = 0; i < M_numSpectators; i++) {
            GenericIO.writelnString("Spec  " + i +  " ID: " + bets[ i ][0]);
            GenericIO.writelnString("Spec  " + i +  " bet horse: " + bets[ i ][1]);
            GenericIO.writelnString("Spec  " + i +  " Value: " + bets[ i ][2]);
        }
    }

    public synchronized void goCollectTheGains()
    {

    }

    //Broker
    public synchronized boolean areThereAnyWinners()
    {
        return false;
    }

    public synchronized void honourTheBets()
    {

    }
}
