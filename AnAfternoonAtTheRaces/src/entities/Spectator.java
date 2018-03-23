package entities;
import sharedRegions.*;

/**
 *  General description:
 *      definition of the spectator.
 */

public class Spectator extends Thread
{

    /**
   *    Internal data
   */

   private final int id;                                      // Spectator thread ID
   private int wallet;                                        // Amount of money Spectator owns
   private SpectatorState state;                // Spectatorstate of the life cycle
   private final ControlCenter controlCenter;
   private final Paddock paddock;
   private final BettingCenter bettingCenter;
   private Logger logger;

   /**
   *    Constructor
   *
   *    @param name Spectator thread name
   *    @param spectatorID Spectator ID
   *    @param money Amount of money Spectator starts with
   */

   public Spectator(String name, int spectatorID, int money, ControlCenter controlCenter,
                        Paddock paddock, BettingCenter bettingCenter,Logger logger)
   {
     super (name);
     this.id = spectatorID;
     this.wallet = money;
     this.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
     this.controlCenter = controlCenter;
     this.paddock = paddock;
     this.bettingCenter = bettingCenter;
     this.logger = logger;
     logger.setMoneyAmount(money,spectatorID);
     logger.setSpectatorState(SpectatorState.WAITING_FOR_A_RACE_TO_START, spectatorID);
   }

  /**
   *   Life cycle
   */

    @Override
    public void run ()
    {
        while( controlCenter.waitForNextRace())         // while there are races
        {                                                                                          // sleep (woken up by last pair horse/jockey to reach paddock)
           controlCenter.goCheckHorses();
           boolean last = paddock.goCheckHorses();
            if(last)
            {                                                                                      // if is last spectator to go reach the paddock
                paddock.lastToCheckHorses();                       // call the horse/jockey pairs
                controlCenter.lastToCheckHorses();
            }
            int horse =  paddock.appraisingHorses();         // sleep (woken up by last horse to leave paddock)

            bettingCenter.placeABet(horse);                     // sleep (woken up by broker when bet's done)

            controlCenter.goWatchTheRace();                  //sleep (woken up by  reportResults() of broker)

            if(controlCenter.haveIWon(horse))
            {
                bettingCenter.goCollectTheGains();            // sleep (woken up by broker when transaction is done)
            }
        }
        controlCenter.relaxABit();                                      // sleep (final state)
    }

    /**
   *   Updates the state of the Spectator
   *
   * @param newState state to update Spectator to
   */
    public void setSpectatorState (SpectatorState newState)
   {
       state = newState;
   }

   /**
   *   Returns the state of the Spectator
   *
   * @return current state of the Spectator
   */
    public SpectatorState getSpectatorState ()
   {
       return this.state;
   }

   /**
   *   Updates  the amount of money currently in the wallet
   *
   * @param transaction amount of money gained or lost
   */
    public void updateWalletValue (int transaction)
   {
       wallet = wallet + transaction;
   }

   /**
   *   Returns the amount of money currently in the wallet
   *
   * @return current value in the wallet
   */
    public int getWalletValue ()
   {
       return this.wallet;
   }

    public int getSpectatorID()
    {
        return this.id;
    }

    @Override
    public String toString() {
        return "Spectator{" +
                "id=" + id +
                ", wallet=" + wallet +
                ", state=" + state +
                '}';
    }
}
