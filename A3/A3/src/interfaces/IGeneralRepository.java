package interfaces;

import states.BrokerState;
import states.HorseJockeyState;
import states.SpectatorState;
import settings.Settings;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General description: Interface defining the operations available over the objects
 * representing the General Repository
 */
public interface IGeneralRepository extends Remote{
    /**
     * Update the state of the Broker in log. Prints the internal state..
     *
     * @param brokerState state to update the Broker to
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setBrokerState(BrokerState brokerState) throws RemoteException;

    /**
     * Set the number of the race currently on the going to be logged and update
     * the Broker's state to ANNOUNCING_NEXT_RACE. Prints the internal state.
     *
     * @param raceNumber number of the current race to be logged
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setRaceNumber(int raceNumber) throws RemoteException;

    /**
     * Set the length of the track in log, in which the races will occur, and
     * therefor the distance to be travelled each race.
     *
     * @param distanceInRace the length of the track
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setDistanceInRace(int[] distanceInRace) throws RemoteException;

    /**
     * Update the amount of money a spectator holds in log.
     *
     * @param spectatorMoneyAmount value of money to update the wallet to in log
     * @param spectatorId          id of the spectator whose wallet is to be
     *                             updated in log.
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setMoneyAmount(int spectatorMoneyAmount, int spectatorId) throws RemoteException;

    /**
     * Update the state of a spectator in log. Prints the internal state.
     *
     * @param spectatorState state to update the spectator in log to
     * @param spectatorId    id of the spectator to be updated in log
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setSpectatorState(SpectatorState spectatorState, int spectatorId) throws RemoteException;

    /**
     * Set the initial state of the Spectator in log. To be called upon creation
     * of respective thread. Useful to log initial state and wallet value in a
     * single operation.
     *
     * @param spectatorState initial state of the Spectator
     * @param spectatorId    id of the Spectator whose initial state is to be
     *                       logged
     * @param money          amount of money the Spectator started with
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setSpectatorInitialState(SpectatorState spectatorState, int spectatorId, int money) throws RemoteException;

    /**
     * Set the information about a Spectator's bet in log.
     *
     * @param spectatorBetAmount    the value of the bet
     * @param spectatorBetSelection the horse id in which the Spectator is
     *                              betting their money
     * @param spectatorMoneyAmount  new balance of the Spectator's wallet
     * @param specId                id of the spectator
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setSpectatorBet(int spectatorBetAmount, int spectatorBetSelection, int spectatorMoneyAmount, int specId) throws RemoteException;

    /**
     * Update the state of a Horse/Jockey pair in log. Prints the internal
     * state.
     *
     * @param horseJockeyState state to update the horse/jockey pair to in log
     * @param horseJockeyId    id of the horse/jockey pair to be updated
     * @param raceId           id of the race the horse/jockey thread is
     *                         assigned to
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setHorseJockeyState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId) throws RemoteException;

    /**
     * Set the initial state of a horse/jockey pair in log. To be called upon
     * creation of respective thread. Useful to log initial state and agility
     * value in a single operation. Only prints out the setting of horse/jockey
     * pairs belonging to the current race, for logging specification reasons.
     *
     * @param horseJockeyState initial state of the horse/jockey pair
     * @param horseJockeyId    id of the horse/jockey pair whose initial state
     *                         is to be set
     * @param raceId           id of the race the horse/jockey thread is
     *                         assigned to
     * @param agility          agility of of the horse/jockey
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setHorseJockeyInitialState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId, int agility) throws RemoteException;

    /**
     * Update the movement of a horse/jockey pair during a race in log. Sets the
     * iteration and the distance travelled.
     *
     * @param horseIteration iteration the horse/jockey pair is currently in
     * @param horsePosition  distance the horse/jockey pair has travelled so far
     *                       in the race
     * @param horseId        id of the horse/jockey pair
     * @param raceId         id of the race the horse/jockey pair is assigned to
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setHorseJockeyMove(int horseIteration, int horsePosition, int horseId, int raceId) throws RemoteException;

    /**
     * Set the stands of the Horse/Jockey pairs at the end of the race in log.
     *
     * @param horsesAtEnd finishing places (stands) of the Horse/Jockey pairs
     * @param raceId      id of the race the horse/jockey pair is assigned to
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void setHorseJockeyStands(int[] horsesAtEnd, int raceId) throws RemoteException;

    /**
     * Return the settings defined.
     *
     * @return an instance of the settings
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    Settings getSettings() throws RemoteException;

    /**
     * Receive and count the votes to decide on whether to start shutdown routine. Execute shutdown on all servers if
     * all entities have voted to do so.
     *
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    void shutdown() throws RemoteException;

    /**
     * Return whether all the servers have been shutdown.
     *
     * @return <code>true</code> if all the servers have been shutdown;
     *         <code>false</code> otherwise
     * @throws RemoteException may be thrown in execution of the remote method call
     */
    boolean hasServiceFinished() throws RemoteException;
}
