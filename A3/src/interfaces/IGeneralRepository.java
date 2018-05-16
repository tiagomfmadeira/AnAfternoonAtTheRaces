package interfaces;

import entities.BrokerState;
import entities.HorseJockeyState;
import entities.SpectatorState;
import settings.Settings;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IGeneralRepository extends Remote{
    void setBrokerState(BrokerState brokerState) throws RemoteException;

    void setRaceNumber(int raceNumber) throws RemoteException;

    void setDistanceInRace(int[] distanceInRace) throws RemoteException;

    void setMoneyAmount(int spectatorMoneyAmount, int spectatorId) throws RemoteException;

    void setSpectatorState(SpectatorState spectatorState, int spectatorId) throws RemoteException;

    void setSpectatorInitialState(SpectatorState spectatorState, int spectatorId, int money) throws RemoteException;

    void setSpectatorBet(int spectatorBetAmount, int spectatorBetSelection, int spectatorMoneyAmount, int specId) throws RemoteException;

    void setHorseJockeyState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId) throws RemoteException;

    void setHorseJockeyInitialState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId, int agility) throws RemoteException;

    void setHorseJockeyMove(int horseIteration, int horsePosition, int horseId, int raceId) throws RemoteException;

    void setHorseJockeyStands(int[] horsesAtEnd, int raceId) throws RemoteException;

    Settings getSettings() throws RemoteException;

    void shutdown() throws RemoteException;

    boolean hasServiceFinished() throws RemoteException;
}
