package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General description: Interface defining the operations available over the objects
 * representing the Paddock
 **/
public interface IPaddock extends Remote {
    /**
     * Changes the state of the horse/jockey pair to AT_THE_PADDOCK and checks
     * whether or not it's the last pair to reach the paddock.
     *
     * @param horseJockeyID the ID of the Horse/Jockey pair
     * @param raceID        the ID of the race
     * @param agility       the agility of the Horse/Jockey pair
     *
     *
     * @return <code>true</code> if it's called by the last horse to reach the
     *         paddock; <code>false</code> otherwise
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean proceedToPaddock(int horseJockeyID, int raceID, int agility) throws RemoteException;

    /**
     * Sleep waiting for a signal that all the spectators have come to the
     * paddock to appraise the horses.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void sleepAtThePaddock() throws RemoteException;

    /**
     * Checks whether it's the last horse/jockey pair to leave the paddock and
     * head to the start line, in which case it wakes up the spectators.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void proceedToStartLine() throws RemoteException;

    /**
     * Changes the Spectator's state to APPRAISING_THE_HORSES. Checks if it's
     * the last Spectator to reach the paddock to appraise the horses, in which
     * case it wakes up the horses sleeping at the paddock.
     *
     * @param specID the ID of the Spectator
     *
     * @return <code>true</code> if it's called by the last Spectator to reach
     *         the paddock; <code>false</code> otherwise
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean goCheckHorses(int specID) throws RemoteException;

    /**
     * Sleep waiting of a signal that the last Horse/Jockey pair has left the
     * paddock. Contains the logic for the betting choices of each spectator,
     * depending on their id. Spectator 0 bets on the Horse/Jockey pair with the
     * most agility; Spectator 1 bets on another Horse/Jockey pair, either with
     * the same agility as the best, or if there isn't such a Horse/Jockey pair,
     * on the second with most agility; Spectator 2 bets on the Horse/Jockey
     * pair with least agility; all other spectators bet randomly.
     *
     * @param specId the ID of the Spectator
     *
     * @return id of the Horse/Jockey pair the Spectator will bet on
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    int appraisingHorses(int specId) throws RemoteException;

    /**
     * Calculate the odds for each of the Horse/Jockey pairs assigned to the
     * current race.
     *
     * @return an array containing the odds for each of the Horse/Jockey pair of
     *         a race indexed by their id in it
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    double[] learnTheOdds() throws RemoteException;

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
