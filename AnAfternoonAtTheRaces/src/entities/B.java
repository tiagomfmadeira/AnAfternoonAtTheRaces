package entities;
import sharedRegions.*;

/**
 *  General description:
 *      definition of the broker..
 */

public class B extends Thread
{

    /**
   *    Internal data
   */

   private int id,                              // Broker thread ID
                         K;                               // number of races
   private state state;                   // Broker state of the life cycle

   /**
   *    Constructor
   *
   *    @param name broker thread name
   *    @param brokerID broker ID
   *    @param numRaces number of races to happen
   */

   public B (String name, int brokerID, int numRaces)
   {
     super (name);
     id = brokerID;
     K = numRaces;
     state = state.OPENING_THE_EVENT;
   }

  /**
   *   Life cycle
   */

   @Override
    public void run ()
    {
        for(int k = 0; k < K - 1; k++)                                            // for each race
        {
            Stable.summonHorsesToPaddock(k);                    // call the horses for a race
            Paddock.summonHorsesToPaddock();                 // sleep (woken up by last last spectator to go see horses)

            ControlCenter.acceptTheBets();                           // sleep (woken up by each spectator to place  bet
                                                                                                        // transition occurs when all have placed bets)

            RaceTrack.startTheRace(k);                                     // call horse/jockey pairs
            ControlCenter.startTheRace();                              // sleep (woken up by last horse to cross finish line)

            ControlCenter.reportResults();                              // call the spectators

            if(BettingCenter.areThereAnyWinners())
            {
                BettingCenter.honourTheBets();                       // sleep (woken up by each winner
                                                                                                        // transition occurs when all winner have been paid)
            }
        }
        ControlCenter.entertainTheGuests();                      //sleep (final state)
    }
}
