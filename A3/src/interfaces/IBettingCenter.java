package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBettingCenter extends Remote{
    void acceptTheBets(double[] horseJockeyOdds) throws RemoteException;

    boolean areThereAnyWinners(boolean[] horseJockeyWinners) throws RemoteException;

    void honourTheBets() throws RemoteException;

    int placeABet(int horseJockeyID, int specId, int walletValue) throws RemoteException;

    int goCollectTheGains(int specID, int walletValue) throws RemoteException;

    void shutdown() throws RemoteException;

    boolean hasServiceFinished() throws RemoteException;
}
