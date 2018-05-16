package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IControlCenter extends Remote{
    // called by the last horse/jockey pair to reach the paddock
    void proceedToPaddock() throws RemoteException;

    // called by the last Horse/Jockey pair to cross the finish line
    void makeAMove() throws RemoteException;

    boolean waitForNextRace(int specId) throws RemoteException;

    void lastToCheckHorses() throws RemoteException;

    void goWatchTheRace(int specId) throws RemoteException;

    boolean haveIWon(int horseJockey) throws RemoteException;

    void relaxABit(int specId) throws RemoteException;

    void summonHorsesToPaddock() throws RemoteException;

    void startTheRace() throws RemoteException;

    void reportResults(boolean[] horseJockeysDeclaredWinners) throws RemoteException;

    void entertainTheGuests() throws RemoteException;

    void shutdown() throws RemoteException;

    boolean hasServiceFinished() throws RemoteException;
}
