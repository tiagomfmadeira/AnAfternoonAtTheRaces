package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IStable extends Remote{
    void summonHorsesToPaddock(int raceID) throws RemoteException;

    void proceedToStable(int raceId) throws RemoteException;

    void proceedToStableFinal(int horseId, int raceId) throws RemoteException;

    void shutdown() throws RemoteException;

    boolean hasServiceFinished() throws RemoteException;
}
