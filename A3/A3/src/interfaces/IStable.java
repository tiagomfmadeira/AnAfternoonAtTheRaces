package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General description: Interface defining the operations available over the objects
 * representing the Stable
 **/
public interface IStable extends Remote{
    /**
     * Wake up the horse/jockey pairs assigned to a race from the AT_THE_STABLE
     * blocking state.
     *
     * @param raceID ID of the race that the horse/jockey pairs to be awaken are
     *               assigned to
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void summonHorsesToPaddock(int raceID) throws RemoteException;

    /**
     * Changes the horse/jockey pair state to AT_THE_STABLE and sleeps waiting
     * for a signal that the next race is starting.
     *
     * @param raceId the ID of the race
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void proceedToStable(int raceId) throws RemoteException;

    /**
     * Changes the horse/jockey pair state to AT_THE_STABLE. Used as a symbolic
     * return to the stable, allowing the thread to finish its life cycle
     * instead of blocking again. This is used because the thread would never be
     * awoken again.
     *
     * @param horseId the ID of the Horse?jockey pair
     * @param raceId  the ID of the race
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void proceedToStableFinal(int horseId, int raceId) throws RemoteException;

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
