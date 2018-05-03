package entities;

import main.SimulPar;
import stub.*;

/**
 * General description: definition of the broker.
 */
public class Broker extends Thread
{

    /**
     * Internal data
     */
    private final int id, numRaces;
    private BrokerState state;
    private final StableStub stable;
    private final ControlCenterStub controlCenter;
    private final PaddockStub paddock;
    private final BettingCenterStub bettingCenter;
    private final RaceTrackStub raceTrack;
    private int currentRace; // current race

    /**
     * Constructor
     *
     * @param name          Broker thread name
     * @param brokerID      Broker ID
     * @param raceTrack     Race Track information sharing region
     * @param stable        Stable information sharing region
     * @param bettingCenter Betting Center information sharing region
     * @param paddock       Paddock information sharing region
     * @param controlCenter Control Center/Watching Stand information sharing
     *                      region
     * @param logger        General Repository of information, keeping a copy of
     *                      the internal state of the problem
     */
    public Broker(String name, int brokerID, RaceTrackStub raceTrack, StableStub stable,
            BettingCenterStub bettingCenter, PaddockStub paddock, ControlCenterStub controlCenter, GeneralRepositoryStub logger)
    {
        super(name);
        this.id = brokerID;
        this.numRaces = SimulPar.K_numRaces;
        this.state = BrokerState.OPENING_THE_EVENT;
        this.controlCenter = controlCenter;
        this.paddock = paddock;
        this.bettingCenter = bettingCenter;
        this.stable = stable;
        this.raceTrack = raceTrack;
        this.currentRace = 0;
        logger.setBrokerState(BrokerState.OPENING_THE_EVENT);
    }

    /**
     * Life cycle
     */
    @Override
    public void run()
    {
        for (currentRace = 0; currentRace < numRaces; currentRace++)     // for each race
        {
            stable.summonHorsesToPaddock(currentRace);                  // call the horses for a race
            controlCenter.summonHorsesToPaddock();                      // sleep (woken up by last last spectator to go see horses)

            double[] odds = paddock.learnTheOdds();
            bettingCenter.acceptTheBets(odds);                          // sleep (woken up by each spectator to place  bet
            // transition occurs when all have placed bets)

            raceTrack.startTheRace();                                   // call horse/jockey pairs
            controlCenter.startTheRace();                               // sleep (woken up by last horse/jockey pair to cross finish line)

            boolean[] horseJockeyWinners = raceTrack.reportResults();   // gather information about winners
            controlCenter.reportResults(horseJockeyWinners);            // call the spectators

            if (bettingCenter.areThereAnyWinners(horseJockeyWinners))
            {
                bettingCenter.honourTheBets();                          // sleep (woken up by each winner
                // transition occurs when all winner have been paid)
            }
        }
        controlCenter.entertainTheGuests();                             //sleep (final state)
    }

    /**
     * Updates the state of the Broker.
     *
     * @param newState state to update the Broker to
     */
    public void setBrokerState(BrokerState newState)
    {
        state = newState;
    }

    /**
     * Returns the current state of the Broker.
     *
     * @return current state of the broker
     */
    public BrokerState getBrokerState()
    {
        return this.state;
    }

    /**
     * Returns the race id the Broker is currently hosting.
     *
     * @return ID of the race the Broker is currently hosting
     */
    public int getCurrentRace()
    {
        return currentRace;
    }

    /**
     * Returns a string representation of the Broker.
     *
     * @return a string representation of the Broker
     */
    @Override
    public String toString()
    {
        return "Broker{"
                + "id=" + id
                + ", numRaces=" + numRaces
                + ", state=" + state
                + '}';
    }
}
