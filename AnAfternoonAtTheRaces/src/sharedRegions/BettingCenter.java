package sharedRegions;

import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;
import java.util.concurrent.ThreadLocalRandom;
import static main.SimulPar.M_numSpectators;
import static main.SimulPar.N_numCompetitors;

/**
 * General description: Definition of the Betting Center information sharing
 * region.
 */
public class BettingCenter implements SharedRegion
{

    /**
     * Internal data
     */
    // saves HorseJockeyID and Value of bet for each Spectator
    private final int[][] bets = new int[M_numSpectators][2];
    // saves odds for each Horse/Jockey
    private final double[] odds = new double[N_numCompetitors];
    private boolean nextSpectatorCanPlaceBet = false,
            lastSpectatorToPlaceBet = false,
            canPlaceBet = false,
            nextSpectatorCanReceiveMoney = false,
            lastSpectatorToReceiveMoney = false,
            canReceiveMoney = false;
    private int numBets = 0,
            numBetsToBeSettled = 0;
    private final GeneralRepository logger;

    /**
     * Constructor
     *
     * @param logger General Repository of information, keeping a copy of the
     *               internal state of the problem
     */
    public BettingCenter(GeneralRepository logger)
    {
        this.logger = logger;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Broker
    /**
     * Sets the odd values for each Horse/Jockey pair of the current race in the
     * Betting Center shared memory. Sleeps waiting for a signal of a spectator
     * wanting to place a bet, wakes up the spectator after the bet has been
     * done and sleeps again, repeating until all the bets have been concluded.
     *
     * @param horseJockeyOdds a vector containing the odds each Horse/Jockey
     *                        pair has to win the current race
     */
    public synchronized void acceptTheBets(double[] horseJockeyOdds)
    {
        // copy the odds
        System.arraycopy(horseJockeyOdds, 0, odds, 0, N_numCompetitors);

        // Change Broker state to WAITING_FOR_BETS
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);
        logger.setBrokerState(BrokerState.WAITING_FOR_BETS);

        //let the first one bet
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
        // everyone has bet
        canPlaceBet = false;
        lastSpectatorToPlaceBet = false;
    }

    /**
     * Checks whether any Spectator bet on a winning Horse/Jockey pair.
     *
     * @param horseJockeyWinners the winning Horse/Jockey pairs
     *
     * @return <code>true</code> if there are any Spectators who won;
     *         <code>false</code> otherwise
     */
    public synchronized boolean areThereAnyWinners(boolean[] horseJockeyWinners)
    {
        // iterate the array of bets
        for (int[] bet : bets)
        {
            // check if the Horse/Jockey pair is in the winner list
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
        // last spec has received money
        canReceiveMoney = false;
        lastSpectatorToReceiveMoney = false;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Spectator
    /**
     * Change the state of the Spectator to PLACING_A_BET. Signal the Broker
     * that there's a Spectator waiting to place a bet. Block waiting for the
     * Broker to accept the placement of the bet. Contains the logic for the
     * betting amounts. Spectator bets depending on their id. Spectator 0 bets
     * 50% of their current money; Spectator 1 bets 25% of their current money;
     * Spectator 2 bets 25% of their current money; all other spectators bet
     * random values, within their respective wallet values. Checks if it's the
     * last Spectator to place the bet, in which case wakes up the Broker,
     * releasing him from the state of accepting bets.
     *
     * @param horseJockeyID id of the Horse/Jockey pair to place a bet on
     */
    public synchronized void placeABet(int horseJockeyID)
    {
        //  Change Spectator state to PLACING_A_BET
        Spectator spec = ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();

        // fill out the bet information
        bets[specId][0] = horseJockeyID;

        int betAmt;

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

        logger.setSpectatorBet(betAmt, horseJockeyID, newWalletValue, specId);

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

    /**
     * Change the state of the Spectator to COLLECTING_THE_GAINS. Signal the
     * Broker that there's a Spectator settle a bet. Block waiting for the
     * Broker to accept the settling of the bet. Checks if it's the last
     * Spectator to settle their bet, in which case wakes up the Broker,
     * releasing him from the state of settling accounts.
     */
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
        int horseJockeyBetOnID = bets[specID][0];
        int winnings = (int) Math.round(bets[specID][1] + bets[specID][1] * (1 / odds[horseJockeyBetOnID]));

        ((Spectator) Thread.currentThread()).updateWalletValue(winnings);
        logger.setMoneyAmount(((Spectator) Thread.currentThread()).getWalletValue(),
                ((Spectator) Thread.currentThread()).getSpectatorID());

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
}
