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

    // saves HorseJockeyID and Value of bet for each Spectator
    int[ ][ ] bets = new int [M_numSpectators ] [2];

    private boolean nextSpectatorCanPlaceBet= false;
    private boolean lastSpectatorToPlaceBet = false;
    private boolean canPlaceBet = false;

    private int numBets = 0;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Broker
        public synchronized void acceptTheBets()
    {
        // Change Broker state to WAITING_FOR_BETS
        ((Broker)Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm waiting for bets!");

        canPlaceBet = true;
        notifyAll();

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

                    GenericIO.writelnString(Thread.currentThread().getName() + " says: I accepted a bet!");

                    canPlaceBet = true;
                    notifyAll();
            }
            catch (InterruptedException e)
            {

            }
        }
        lastSpectatorToPlaceBet = false;
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I have accepted all the bets!");
    }

    public synchronized void honourTheBets()
    {

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Spectator
    public synchronized boolean placeABet(int horseID)
    {
        boolean isLastSpectator = false;

        //  Change Spectator state to PLACING_A_BET
        ((Spectator)Thread.currentThread()).setSpectatorState(SpectatorState.PLACING_A_BET);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm filling in the bet information!");

        // Saved information about the bet, maybe do this in broker thread??
        int specID = ((Spectator)Thread.currentThread()).getSpectatorID();
        bets[specID][0] = horseID;
        // TODO improve logic of bet value
        bets[specID][1] = ((Spectator)Thread.currentThread()).getWalletValue();

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

        numBets++;

        GenericIO.writelnString(Thread.currentThread().getName() + " says: My bet has been acepted!!");
        // TODO subtract the money from wallet??

        if(numBets == N_numCompetitors)
        {
           GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm the last spectator to bet!");
            isLastSpectator = true;
            numBets = 0;
            lastSpectatorToPlaceBet = true;
            //notifyAll();
        }
            nextSpectatorCanPlaceBet = true;
            notifyAll();

        return isLastSpectator;
    }

    public synchronized void lastToPlaceBet()
    {
        GenericIO.writelnString("Betting information stored in Betting center: ");
        for (int i = 0; i < M_numSpectators; i++) {
            GenericIO.writelnString("Spec  " + i +  " bet horse: " + bets[ i ][0]);
            GenericIO.writelnString("Spec  " + i +  " Value: " + bets[ i ][1]);
        }
    }

    public synchronized void goCollectTheGains()
    {

    }
}
