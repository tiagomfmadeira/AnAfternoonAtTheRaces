package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General description: Interface defining the operations available over the objects
 * representing the Race Track
 **/
public interface IRaceTrack extends Remote {
    /**
     * Changes the Horse/Jockey pair state to AT_THE_START_LINE and sleeps
     * waiting for a signal from the Broker that the race has started, or a
     * signal from a move of another Horse/Jockey pair. When woken up changes
     * the state of the Horse/Jockey pair to RUNNING.
     *
     * @param horseJockeyId the ID of the Horse/Jockey pair
     * @param raceId        the ID of the race
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void proceedToStartLine(int horseJockeyId, int raceId) throws RemoteException;

    /**
     * The Horse/Jockey pair blocks after carrying out a position increment,
     * unless he crosses the finishing line. Check if the Horse/Jockey pair has
     * crossed the finish line, in which case it records that it's standing in
     * the finish line and changes its state to AT_THE_FINNISH_LINE. Returns
     * whether it's the last Horse/Jockey pair to cross the finish line. If it's
     * the last Horse/Jockey pair crossing the finish line, wake up all the
     * Horse/Jockey pairs standing in the finish line.
     *
     * @param horseJockeyId the ID of the Horse/Jockey pair
     * @param raceId        the ID of the race
     *
     * @param agility       the agility of the Horse/Jockey pair
     *
     * @return <code>true</code> if it's the last Horse/Jockey pair to cross the
     *         finish line; <code>false</code> otherwise
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean makeAMove(int horseJockeyId, int raceId, int agility) throws RemoteException;

    /**
     * Checks whether the race has ended or not.
     *
     * @param raceId the ID of the race
     *
     * @return <code>true</code> if the race has ended; <code>false</code>
     *         otherwise
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean hasRaceEnded(int raceId) throws RemoteException;

    /**
     * Change the Broker state to SUPERVISING_THE_RACE. Wake up one of the
     * Horse/Jockey pairs from AT_THE_START_LINE.
     *
     * @param raceId the ID of the race
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void startTheRace(int raceId) throws RemoteException;

    /**
     * Checks which Horse/Jockey pairs have won the race. Logs the standings of
     * the race.
     *
     * @param raceId the ID of the race
     *
     * @return array indicating for each Horse/Jockey pair if they have won the
     *         race, indexed by their ID
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean[] reportResults(int raceId) throws RemoteException;

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
