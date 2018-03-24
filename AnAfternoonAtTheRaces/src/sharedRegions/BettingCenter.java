package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;
import java.util.concurrent.ThreadLocalRandom;

import static main.SimulPar.N_numCompetitors;
import static main.SimulPar.M_numSpectators;
import static main.SimulPar.N_numCompetitors;

public class BettingCenter
{

    // saves HorseJockeyID and Value of bet for each Spectator
    private int[][] bets = new int[M_numSpectators][2];
    // saves odds for each Horse/Jockey
    private double[] odds = new double[N_numCompetitors];

    private boolean nextSpectatorCanPlaceBet = false,
            lastSpectatorToPlaceBet = false,
            canPlaceBet = false,
            nextSpectatorCanReceiveMoney = false,
            lastSpectatorToReceiveMoney = false,
            canReceiveMoney = false;

    private int numBets = 0,
            numBetsToBeSettled = 0;

    private Logger logger;

    public BettingCenter(Logger logger)
    {
        this.logger = logger;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Broker
    /**
     * Sets the odd values for each horse of the current race in the Betting
     * Center shared memory. Sleeps waiting for a signal of a spectator wanting
     * to place a bet, wakes up the spectator after the bet has been done and
     * sleeps again, repeating until all the bets have been concluded.
     *
     * @param horseOdds A vector containing the odds each horse/jockey pair has
     *                  to win the current race
     */
    public synchronized void acceptTheBets(double[] horseOdds)
    {
        // copy the odds
        System.arraycopy(horseOdds, 0, odds, 0, N_numCompetitors);
        int currentRace = ((Broker) Thread.currentThread()).getCurrentRace();
        logger.setHorseOdds(odds, currentRace);

        // Change Broker state to WAITING_FOR_BETS
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);
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
                    } catch (InterruptedException e)
                    {
                        throw e;
                    }
                }

                nextSpectatorCanPlaceBet = false;

                canPlaceBet = true;
                notifyAll();
            } catch (InterruptedException e)
            {

            }
        }
        lastSpectatorToPlaceBet = false;
    }

    public synchronized boolean areThereAnyWinners(boolean[] horseJockeyWinners)
    {
        // iterate the array of bets
        for (int[] bet : bets)
        {
            // check if the horse is in the winner list
            if (horseJockeyWinners[bet[0]] == true)
            {
                numBetsToBeSettled++;
            }
        }
        return numBetsToBeSettled > 0;
    }

    /**
     * Sleeps waiting for a signal of a spectator wanting to receive their
     * gains, wakes up the spectator after the bet has been settled and sleeps
     * again, repeating until all the bets have been settled.
     */
    public synchronized void honourTheBets()
    {
        // Change Broker state to SETTLING_ACCOUNTS
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.SETTLING_ACCOUNTS);
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
                    } catch (InterruptedException e)
                    {
                        throw e;
                    }
                }

                nextSpectatorCanReceiveMoney = false;

                canReceiveMoney = true;
                notifyAll();
            } catch (InterruptedException e)
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
        Spectator spec = ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();
        spec.setSpectatorState(SpectatorState.PLACING_A_BET);
        logger.setSpectatorState(SpectatorState.PLACING_A_BET, specId);

        while (!canPlaceBet)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        canPlaceBet = false;

        // fill out the bet information
        bets[specId][0] = horseID;

        int betAmt = 0;

        switch (specId)
        {
            // bet 50%
            case 0:
                betAmt = (int) Math.floor(((Spectator) Thread.currentThread()).getWalletValue() * 0.5);
                break;
            // bet 25%
            case 1:
                betAmt = (int) Math.floor(((Spectator) Thread.currentThread()).getWalletValue() * 0.25);
                break;
            // bet 25%
            case 2:
                betAmt = (int) Math.floor(((Spectator) Thread.currentThread()).getWalletValue() * 0.25);
                break;
            // bet random
            default:
                int wallet = ((Spectator) Thread.currentThread()).getWalletValue();
                betAmt = ThreadLocalRandom.current().nextInt(0, wallet + 1);
                break;
        }

        bets[specId][1] = betAmt;

        ((Spectator) Thread.currentThread()).updateWalletValue(-bets[specId][1]);
        int newWalletValue = ((Spectator) Thread.currentThread()).getWalletValue();

        logger.setSpectatorBet(betAmt, horseID, newWalletValue, specId);

        numBets++;

        // is last spectator
        if (numBets == M_numSpectators)
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
        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.COLLECTING_THE_GAINS);
        logger.setSpectatorState(SpectatorState.COLLECTING_THE_GAINS,
                ((Spectator) Thread.currentThread()).getSpectatorID());

        while (!canReceiveMoney)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
        canReceiveMoney = false;

        // settlement accepted, update wallet with winnings
        int specID = ((Spectator) Thread.currentThread()).getSpectatorID();
        int horseBetOnID = bets[specID][0];
        int winnings = (int) Math.round(bets[specID][1] + bets[specID][1] * (1 / odds[horseBetOnID]));

        ((Spectator) Thread.currentThread()).updateWalletValue(winnings);
        logger.setMoneyAmount(((Spectator) Thread.currentThread()).getWalletValue(), ((Spectator) Thread.currentThread()).getSpectatorID());

        numBetsToBeSettled--;

        // if is last winner to reclaim winnings
        if (numBetsToBeSettled == 0)
        {
            // release broker
            lastSpectatorToReceiveMoney = true;
            notifyAll();
        }
        // wake up broker for next bet settle
        nextSpectatorCanReceiveMoney = true;
        notifyAll();
    }

    public int[][] getBets()
    {
        return bets;
    }

    public double[] getOdds()
    {
        return odds;
    }
}
