package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRaceTrack extends Remote {
    void proceedToStartLine(int horseJockeyId, int raceId) throws RemoteException;

    boolean makeAMove(int horseJockeyId, int raceId, int agility) throws RemoteException;

    boolean hasRaceEnded(int raceId) throws RemoteException;

    void startTheRace(int raceId) throws RemoteException;

    boolean[] reportResults(int raceId) throws RemoteException;

    void shutdown() throws RemoteException;

    boolean hasServiceFinished() throws RemoteException;
}
