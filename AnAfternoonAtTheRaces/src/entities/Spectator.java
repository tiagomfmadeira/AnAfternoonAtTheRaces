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

   private int id;                                             // Spectator thread ID
   private SpectatorState state;                                  // Spectatorstate of the life cycle
   private ControlCenter controlCenter;
   private Paddock paddock;
   private BettingCenter bettingCenter;

   /**
   *    Constructor
   *
   *    @param name Spectator thread name
   *    @param spectatorID Spectator ID
   */

   public Spectator(String name, int spectatorID,
                    ControlCenter controlCenter, Paddock paddock, BettingCenter bettingCenter)
   {
     super (name);
     this.id = spectatorID;
     this.state = SpectatorState.WAITING_FOR_A_RACE_TO_START;
     this.controlCenter = controlCenter;
     this.paddock = paddock;
     this.bettingCenter = bettingCenter;

   }

  /**
   *   Life cycle
   */

    @Override
    public void run ()
    {
        while( controlCenter.waitForNextRace())         // while there are races
        {                                                                                          // sleep (woken up by last pair horse/jockey to reach paddock)
            boolean last = controlCenter.goCheckHorses();
            if(last)
            {                                                                                      // if is last spectator to go check horses
                paddock.lastToCheckHorses();                       // call the horse/jockey pairs and Broker
            }
            paddock.goCheckHorses();                                  // sleep (woken up by last horse to leave paddock)

            last = bettingCenter.placeABet();                     // sleep (woken up by broker when bet's done)
            if(last)
            {                                                                                       // if is last to place a bet
                bettingCenter.lastToPlaceBet();                    // call Broker
            }

            controlCenter.goWatchTheRace();                  //sleep (woken up by  reportResults() of broker)

            if(controlCenter.haveIWon())
            {
                bettingCenter.goCollectTheGains();            // sleep (woken up by broker when transaction is done)
            }
        }
        controlCenter.relaxABit();                                      // sleep (final state)
    }
}
