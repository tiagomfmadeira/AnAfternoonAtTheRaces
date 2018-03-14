package entities;
import sharedRegions.*;

/**
 *  General description:
 *      definition of the horse/jockey.
 */

public class HJ extends Thread
{

    /**
   *    Internal data
   */

   private int id,                                             // Horse/Jockey thread ID
                         agility;                                     // size of pace during race
   private state state;                                 // Horse/Jockey state of the life cycle

   /**
   *    Constructor
   *
   *    @param name Horse/Jockey thread name
   *    @param HJID Horse/Jockey ID
   *    @param agility Horse agility
   */

   public HJ (String name, int HJID, int agility)
   {
     super (name);
     id = HJID;
     this.agility = agility;
     state = state.AT_THE_STABLE;
   }

  /**
   *   Life cycle
   */

    @Override
    public void run ()
    {
        last = Stable.proceedToStable();
        if(last)
        {                                                                                     // if is last horse to go to paddock
            ControlCenter.proceedToPaddock();          // call spectators
        }
        Paddock.proceedToPaddock();                         // sleep (woken up by last last spectator to go see horses)

        last = Paddock.proceedToStartLine();
        if(last)
        {                                                                                      // if  is last horse to go to start line
            Paddock.lastProceedToStartLine();             // call spectators
        }
       RaceTrack. proceedToStartLine();                     // sleep (woken up by broker with startTheRace()
                                                                                              //             or by another horse with makeAMove())

        do
        {
            RaceTrack. makeAMove();                             // sleep (woken up by previous horse)
        } while(!RaceTrack. hasFinishLineBeenCrossed());

        Stable. proceedToStable();                               // sleep (final state)
    }
}