package entities;

/**
 *  General description:
 *      definition of the broker..
 */

public class B extends thread
{

    /**
   *    Internal data
   */

   private int id;                                            // Broker thread ID
   private STATE state;                             // Broker state

   /**
   *    Constructor
   *
   *    @param name broker thread name
   *    @param brokerID broker ID
   */

   public HJ (String name, int brokerID)
   {
     super (name);
     id = HJID;
     
   }

  /**
   *   Life cycle
   */

public class B
{
    void run ()
    {
        for(int k = 0; k < K - 1; k++)                  // K is the number of races
        {
            Stable.summonHorsesToPaddock(raceID);       // call the horses for this race
            Paddock.summonHorsesToPaddock();                // sleep

            Paddock.acceptTheBets();                                       // wake
            ControlCenter.acceptTheBets();                            // sleep

            RaceTrack.startTheRace();

            ControlCenter.reportResults();

            if(RaceTrack.areThereAnyWinners())
            {
                BettingCenter.honourTheBets();
            }
            ControlCenter.entertainTheGuests();
        }
    }
}
