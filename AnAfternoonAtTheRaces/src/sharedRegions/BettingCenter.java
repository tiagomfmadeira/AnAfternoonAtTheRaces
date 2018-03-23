package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;

import static main.SimulPar.N_numCompetitors;
import static main.SimulPar.M_numSpectators;

public class BettingCenter {

    // saves HorseJockeyID and Value of bet for each Spectator
    private int[ ][ ] bets = new int [M_numSpectators] [2];
    // saves odds for each Horse/Jockey
    private double [ ] odds = new double [N_numCompetitors];

    private boolean nextSpectatorCanPlaceBet= false,
                                    lastSpectatorToPlaceBet = false,
                                    canPlaceBet = false,
                                    nextSpectatorCanReceiveMoney= false,
                                    lastSpectatorToReceiveMoney = false,
                                    canReceiveMoney = false;

    private int numBets = 0,
                          numBetsToBeSettled = 0;

    private Logger logger;

    public BettingCenter(Logger logger){
        this.logger = logger;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Broker
        public synchronized void acceptTheBets(double[ ] horseOdds)
    {
        // copy the odds
        System.arraycopy(horseOdds, 0, odds, 0, N_numCompetitors);
        int currentRace = ((Broker)Thread.currentThread()).getCurrentRace();
        logger.setHorseOdds(odds, currentRace);

        // Change Broker state to WAITING_FOR_BETS
        ((Broker)Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);
        logger.setBrokerState(BrokerState.WAITING_FOR_BETS);


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

                    canPlaceBet = true;
                    notifyAll();
            }
            catch (InterruptedException e)
            {

            }
        }
        lastSpectatorToPlaceBet = false;
    }

    public synchronized boolean areThereAnyWinners(boolean [ ] horseJockeyWinners)
    {
        // iterate the array of bets
        for (int[ ] bet : bets)
        {
            // check if the horse is in the winner list
            if (horseJockeyWinners[bet[ 0 ]] == true)
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
        logger.setBrokerState(BrokerState.SETTLING_ACCOUNTS);

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


                canReceiveMoney = true;
                notifyAll();
            }
            catch (InterruptedException e)
            {

            }
        }
        lastSpectatorToReceiveMoney = false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Spectator
    public synchronized void placeABet(int horseID)
    {
        //  Change Spectator state to PLACING_A_BET
        Spectator spec = ((Spectator)Thread.currentThread());
        int specId = spec.getSpectatorID();
        spec.setSpectatorState(SpectatorState.PLACING_A_BET);
        logger.setSpectatorState(SpectatorState.PLACING_A_BET, specId);

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

        // fill out the bet information
        bets[specId][0] = horseID;
        logger.setSpectatorBetSelection(horseID, specId);
        // TODO: improve logic of bet value
        int betAmt = (int) Math.floor(((Spectator)Thread.currentThread()).getWalletValue()*0.25);
        bets[specId][1] = betAmt;
        logger.setSpectatorBetAmount(betAmt, specId);

        ((Spectator)Thread.currentThread()).updateWalletValue(- bets[specId][1]);

        numBets++;

        // is last spectator
        if(numBets == M_numSpectators)
        {
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
        logger.setSpectatorState(SpectatorState.COLLECTING_THE_GAINS,
                ((Spectator) Thread.currentThread()).getSpectatorID() );

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

        numBetsToBeSettled--;

        // if is last winner to reclaim winnings
        if(numBetsToBeSettled == 0)
        {

           // release broker
            lastSpectatorToReceiveMoney = true;
            notifyAll();
        }
            // wake up broker for next bet settle
            nextSpectatorCanReceiveMoney = true;
            notifyAll();
    }

    public int[][] getBets() {
        return bets;
    }

    public double[] getOdds() {
        return odds;
    }
}
