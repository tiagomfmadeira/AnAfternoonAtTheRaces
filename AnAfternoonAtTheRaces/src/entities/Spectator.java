package entities;

import sharedRegions.*;

/**
 * General description: definition of the spectator.
 */
public class Spectator extends Thread
{

    /**
     * Internal data
     */
    private final int id;                       // Spectator thread ID
    private int wallet;                         // Amount of money Spectator owns
    private SpectatorState state;               // Spectatorstate of the life cycle
    private final ControlCenter controlCenter;
    private final Paddock paddock;
    private final BettingCenter bettingCenter;

    /**
     * Constructor
     *
     * @param name          Spectator thread name
     * @param spectatorID   Spectator ID
     * @param money         Amount of money Spectator starts with
     * @param controlCenter Control Center/Watching Stand information sharing
     *                      region
     * @param paddock       Paddock information sharing region
     * @param bettingCenter Betting Center information sharing region
     * @param logger        General Repository of information, keeping a copy of
     *                      the internal state of the problem
     */
    public Spectator(String name, int spectatorID, int money,
            ControlCenter controlCenter, Paddock paddock,
            BettingCenter bettingCenter, GeneralRepository logger)
    {
        super(name);
        this.id = spectatorID;
        this.wallet = money;
        this.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
        this.controlCenter = controlCenter;
        this.paddock = paddock;
        this.bettingCenter = bettingCenter;
        logger.setSpectatorInitialState(SpectatorState.WAITING_FOR_A_RACE_TO_START, spectatorID, money);
    }

    /**
     * Life cycle
     */
    @Override
    public void run()
    {
        while (controlCenter.waitForNextRace())             // while there are races
        {                                                   // sleep (woken up by last pair Horse/Jockey pair to reach paddock)
            boolean last = paddock.goCheckHorses();
            if (last)
            {                                               // if is last spectator to go reach the paddock
                controlCenter.lastToCheckHorses();
            }
            int horseJockeyId = paddock.appraisingHorses(); // sleep (woken up by last Horse/Jockey pair to leave paddock)

            bettingCenter.placeABet(horseJockeyId);         // sleep (woken up by broker when bet's done)

            controlCenter.goWatchTheRace();                 //sleep (woken up by  reportResults() of broker)

            if (controlCenter.haveIWon(horseJockeyId))
            {
                bettingCenter.goCollectTheGains();          // sleep (woken up by broker when transaction is done)
            }
        }
        controlCenter.relaxABit();                          // sleep (final state)
    }

    /**
     * Updates the state of the Spectator.
     *
     * @param newState state to update the Spectator to
     */
    public void setSpectatorState(SpectatorState newState)
    {
        state = newState;
    }

    /**
     * Returns the current state of the Spectator.
     *
     * @return current state of the Spectator
     */
    public SpectatorState getSpectatorState()
    {
        return this.state;
    }

    /**
     * Updates the amount of money currently in the spectator's wallet by adding
     * a transaction value to the current balance.
     *
     * @param transaction amount of money gained or lost. positive if gained;
     *                    negative if lost
     */
    public void updateWalletValue(int transaction)
    {
        wallet = wallet + transaction;
    }

    /**
     * Returns the amount of money currently in the wallet.
     *
     * @return amount of money currently in the wallet
     */
    public int getWalletValue()
    {
        return this.wallet;
    }

    /**
     * Returns the ID of the spectator.
     *
     * @return id of the spectator
     */
    public int getSpectatorID()
    {
        return this.id;
    }

    /**
     * Returns a string representation of the Spectator.
     *
     * @return a string representation of the Spectator
     */
    @Override
    public String toString()
    {
        return "Spectator{"
                + "id=" + id
                + ", wallet=" + wallet
                + ", state=" + state
                + '}';
    }
}
