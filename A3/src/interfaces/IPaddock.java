package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPaddock extends Remote {
    boolean proceedToPaddock(int horseJockeyID, int raceID, int agility) throws RemoteException;

    void sleepAtThePaddock() throws RemoteException;

    void proceedToStartLine() throws RemoteException;

    boolean goCheckHorses(int specID) throws RemoteException;

    int appraisingHorses(int specId) throws RemoteException;

    double[] learnTheOdds() throws RemoteException;

    void shutdown() throws RemoteException;

    boolean hasServiceFinished() throws RemoteException;
}
