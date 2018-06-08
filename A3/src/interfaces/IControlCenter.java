package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General description: Interface defining the operations available over the objects
 * representing the Control Center
 **/
public interface IControlCenter extends Remote{
    /**
     * Wake up the Spectators from the WAITING_FOR_A_RACE_TO_START blocking
     * state.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void proceedToPaddock() throws RemoteException;

    /**
     * Wake up the Broker from the SUPERVISING_THE_RACE blocking state.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void makeAMove() throws RemoteException;

    /**
     * Changes the spectator state to WAITING_FOR_A_RACE_TO_START and sleeps
     * waiting for a signal that the next race is starting or that there are no
     * more races. Returns weather there's a next race or not.
     *
     * @param specId the ID of the spectator
     *
     * @return <code>true</code> if there is a next race; <code>false</code>
     *         otherwise
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean waitForNextRace(int specId) throws RemoteException;

    /**
     * Wake up the Broker from the SUPERVISING_THE_RACE blocking state.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void lastToCheckHorses() throws RemoteException;

    /**
     * Change Spectator state to WATCHING_A_RACE and sleep waiting for a signal
     * that the race has ended.
     *
     * @param specId the ID of the spectator
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void goWatchTheRace(int specId) throws RemoteException;

    /**
     * Called by a Spectator to check whether the Horse/Jockey pair they bet on
     * won.
     *
     * @param horseJockey id of the Horse/Jockey pair the Spectator bet on
     *
     * @return <code>true</code> if the Horse/Jockey pair provided won the race;
     *         <code>false</code> otherwise
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean haveIWon(int horseJockey) throws RemoteException;

    /**
     * Change the Spectator state to CELEBRATING. Final state of their life
     * cycle.
     *
     * @param specId the ID of the spectator
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void relaxABit(int specId) throws RemoteException;

    /**
     * Sleeps waiting for a signal that all the Spectators have appraised the
     * horses.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void summonHorsesToPaddock() throws RemoteException;

    /**
     * Sleep waiting for a signal that the last Horse/Jockey pair has crossed
     * the finish line.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void startTheRace() throws RemoteException;

    /**
     * Place information on what Horse/Jockey pairs won the race in the shared
     * region. Wake up the spectators from WATCHING_A_RACE blocking state.
     *
     * @param horseJockeysDeclaredWinners array indicating for each Horse/Jockey
     *                                    pair if they have won the race,
     *                                    indexed by their ID
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void reportResults(boolean[] horseJockeysDeclaredWinners) throws RemoteException;

    /**
     * Change the Broker state to PLAYING_HOST_AT_THE_BAR. Final state of their
     * life cycle.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void entertainTheGuests() throws RemoteException;

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
