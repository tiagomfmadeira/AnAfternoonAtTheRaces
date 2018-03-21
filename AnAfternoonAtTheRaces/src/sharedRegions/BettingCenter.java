package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;
import genclass.GenericIO;
import java.util.HashSet;

import static main.SimulPar.N_numCompetitors;
import static main.SimulPar.M_numSpectators;

public class BettingCenter {

    // saves HorseJockeyID and Value of bet for each Spectator
    int[ ][ ] bets = new int [M_numSpectators ] [2];
    // saves odds for each Horse/Jockey
    double [ ] odds = new double [N_numCompetitors];

    private boolean nextSpectatorCanPlaceBet= false,
                                    lastSpectatorToPlaceBet = false,
                                    canPlaceBet = false,
                                    nextSpectatorCanReceiveMoney= false,
                                    lastSpectatorToReceiveMoney = false,
                                    canReceiveMoney = false;

    private int numBets = 0,
                          numBetsToBeSettled = 0;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Broker
        public synchronized void acceptTheBets(double[ ] horseOdds)
    {
        // copy the odds
        System.arraycopy(horseOdds, 0, odds, 0, N_numCompetitors) ;

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

    public synchronized boolean areThereAnyWinners(HashSet horseJockeyWinners)
    {
        // iterate the array of bets
        for (int[ ] bet : bets)
        {
            // check if the horse is in the winner list
            if (horseJockeyWinners.contains(bet[ 0 ]))
            {
                numBetsToBeSettled++;
            }
        }
        return numBetsToBeSettled > 0;
    }

    public synchronized void honourTheBets()
    {
        // Change Broker state to SETTLING_ACCOUNTS
        ((Broker)Thread.currentThread()).setBrokerState(BrokerState.SETTLING_ACCOUNTS);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm waiting to settle bets!");

        canReceiveMoney = true;
        notifyAll();

        while (!lastSpectatorToReceiveMoney)
        {
            try
            {
                while (!nextSpectatorCanReceiveMoney)
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

                nextSpectatorCanReceiveMoney = false;

                GenericIO.writelnString(Thread.currentThread().getName() + " says: I have settled a bet!");

                canReceiveMoney = true;
                notifyAll();
            }
            catch (InterruptedException e)
            {

            }
        }
        lastSpectatorToReceiveMoney = false;
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I have honoured all the bets!");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Spectator
    public synchronized void placeABet(int horseID)
    {
        //  Change Spectator state to PLACING_A_BET
        ((Spectator)Thread.currentThread()).setSpectatorState(SpectatorState.PLACING_A_BET);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm filling in the bet information!");

        // fill out the bet information
        int specID = ((Spectator)Thread.currentThread()).getSpectatorID();
        bets[specID][0] = horseID;
        // TODO: improve logic of bet value
        bets[specID][1] = (int) Math.floor(((Spectator)Thread.currentThread()).getWalletValue()*0.25);

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

        ((Spectator)Thread.currentThread()).updateWalletValue(- bets[specID][1]);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: My bet has been acepted!!");

        numBets++;

        // is last spectator
        if(numBets == M_numSpectators)
        {
           GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm the last spectator to bet!");
           numBets = 0;

           // release broker
            lastSpectatorToPlaceBet = true;
            notifyAll();
        }
            // wake up broker to accept next bet
            nextSpectatorCanPlaceBet = true;
            notifyAll();
    }

    public synchronized void goCollectTheGains()
    {
        //  Change Spectator state to COLLECTING_THE_GAINS
        ((Spectator)Thread.currentThread()).setSpectatorState(SpectatorState.COLLECTING_THE_GAINS);
        GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm waiting to receive the money!");

        while (!canReceiveMoney)
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }
        canReceiveMoney = false;

        // settlement accepted, update wallet with winnings
        int specID = ((Spectator)Thread.currentThread()).getSpectatorID();
        int horseBetOnID = bets[specID][0];
        int winnings =  (int) Math.round( bets[specID][1]  +  bets[specID][1] * (1 / odds[ horseBetOnID ]) );

        ((Spectator)Thread.currentThread()).updateWalletValue(winnings);
        GenericIO.writelnString(Thread.currentThread().getName() + " My wallet: " + ((Spectator)Thread.currentThread()).getWalletValue() );

        GenericIO.writelnString(Thread.currentThread().getName() + " says: My bet has been settled!");

        numBetsToBeSettled--;

        // if is last winner to reclaim winnings
        if(numBetsToBeSettled == 0)
        {
           GenericIO.writelnString(Thread.currentThread().getName() + " says: I'm the last spectator to settle bet!");

           // release broker
            lastSpectatorToReceiveMoney = true;
            notifyAll();
        }
            // wake up broker for next bet settle
            nextSpectatorCanReceiveMoney = true;
            notifyAll();
    }
}
