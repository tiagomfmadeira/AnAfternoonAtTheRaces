package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General description: Interface defining the operations available over the objects
 * representing the Betting Center
 **/
public interface IBettingCenter extends Remote{

    /**
     * Sets the odd values for each Horse/Jockey pair of the current race in the
     * Betting Center shared memory. Sleeps waiting for a signal of a spectator
     * wanting to place a bet, wakes up the spectator after the bet has been
     * done and sleeps again, repeating until all the bets have been concluded.
     *
     * @param horseJockeyOdds a vector containing the odds each Horse/Jockey
     *                        pair has to win the current race
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void acceptTheBets(double[] horseJockeyOdds) throws RemoteException;

    /**
     * Checks whether any Spectator bet on a winning Horse/Jockey pair.
     *
     * @param horseJockeyWinners the winning Horse/Jockey pairs
     *
     * @return <code>true</code> if there are any Spectators who won;
     *         <code>false</code> otherwise
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean areThereAnyWinners(boolean[] horseJockeyWinners) throws RemoteException;

    /**
     * Sleeps waiting for a signal of a spectator wanting to receive their
     * gains, wakes up the spectator after the bet has been settled and sleeps
     * again, repeating until all the bets have been settled.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void honourTheBets() throws RemoteException;

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
     * @param horseJockeyID the id of the Horse/Jockey pair to place a bet on
     * @param specId        the id of the spectator placing the bet
     * @param walletValue   the value in the wallet of the spectator placing the
     *                      bet
     *
     * @return the value of the wager placed
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    int placeABet(int horseJockeyID, int specId, int walletValue) throws RemoteException;

    /**
     * Change the state of the Spectator to COLLECTING_THE_GAINS. Signal the
     * Broker that there's a Spectator settle a bet. Block waiting for the
     * Broker to accept the settling of the bet. Checks if it's the last
     * Spectator to settle their bet, in which case wakes up the Broker,
     * releasing him from the state of settling accounts.
     *
     * @param specID      the id of the spectator placing the bet
     * @param walletValue the value in the wallet of the spectator placing the
     *                    bet
     *
     * @return value won in the wager
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    int goCollectTheGains(int specID, int walletValue) throws RemoteException;

    /**
     * Changes a boolean variable state to true, symbolising the conclusion of
     * the service.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void shutdown() throws RemoteException;

    /**
     * Checks whether the service has been completed.
     *
     * @return <code>true</code> if the service has been completed
     *         <code>false</code> otherwise
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean hasServiceFinished() throws RemoteException;
}
