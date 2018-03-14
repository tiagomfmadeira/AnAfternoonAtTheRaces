package entities;
import sharedRegions.*;

/**
 *  General description:
 *      definition of the spectator.
 */

public class S extends Thread
{

    /**
   *    Internal data
   */

   private int id;                                             // Spectator thread ID
   private state state;                                  // Spectatorstate of the life cycle

   /**
   *    Constructor
   *
   *    @param name Spectator thread name
   *    @param spectatorID Spectator ID
   */

   public S (String name, int spectatorID)
   {
     super (name);
     id = spectatorID;
     state = state.WAITING_FOR_A_RACE_TO_START;
   }

  /**
   *   Life cycle
   */

    @Override
    public void run ()
    {
        while(ControlCenter.waitForNextRace())         // while there are races
        {                                                                                          // sleep (woken up by last pair horse/jockey to reach paddock)
            last = ControlCenter.goCheckHorses();
            if(last)
            {                                                                                      // if is last spectator to go check horses
                Paddock.lastToCheckHorses();                       // call the horse/jockey pairs and Broker
            }
            Paddock.goCheckHorses();                                  // sleep (woken up by last horse to leave paddock)

            last = BettingCenter.placeABet();                     // sleep (woken up by broker when bet's done)
            if(last)
            {                                                                                       // if is last to place a bet
                BettingCenter.lastToPlaceBet();                    // call Broker
            }

            ControlCenter.goWatchTheRace();                  //sleep (woken up by  reportResults() of broker)

            if(ControlCenter.haveIWon())
            {
                BettingCenter.goCollectTheGains();            // sleep (woken up by broker when transaction is done)
            }
        }
        ControlCenter.relaxABit();                                      // sleep (final state)
    }
}
