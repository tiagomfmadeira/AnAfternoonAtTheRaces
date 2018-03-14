package entities;

/**
 *  General description:
 *      definition of the horse/jockey.
 */

public class HJ extends thread
{

    /**
   *    Internal data
   */

   private int id,                                                              // Horse/Jockey thread ID
                         agility;                                                      // size of increments during race

   /**
   *    Constructor
   *
   *    @param name thread name
   *    @param HJID horse/jockey ID
   *    @param agility horse agility
   */

   public HJ (String name, int HJID, int agility)
   {
     super (name);
     id = HJID;
     this.agility = agility;
   }

  /**
   *   Life cycle
   */

    @Override
    void run ()
    {
        last = Stable.proceedToStable();                      // wake horse
        if(last)
        {                                                                                     // if last horse
            ControlCenter.proceedToPaddock();          // call specs
        }
        Paddock.proceedToPaddock();                         // sleep

        last = Paddock.proceedToStartLine();            // wake horse
        if(last)
        {                                                                                     // if last horse
            Paddock.lastProceedToStartLine();             // call specs
        }
       RaceTrack. proceedToStartLine();                    // sleep

        do
        {
            RaceTrack. makeAMove();
        } while(!RaceTrack. hasFinishLineBeenCrossed());

        RaceTrack. proceedToStable();           // wake horse
        Stable. proceedToStable();                   // sleep
    }
}