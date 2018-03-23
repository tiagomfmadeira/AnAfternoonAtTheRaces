package entities;
import sharedRegions.*;

/**
 *  General description:
 *      definition of the horse/jockey.
 */

public class HorseJockey extends Thread
{

    /**
   *    Internal data
   */

    private final int id,                                    // Horse/Jockey thread ID
                                    agility,
                                    raceId;                           // size of pace during race
    private HorseJockeyState state;        // Horse/Jockey state of the life cycle
    private final Stable stable;
    private final Paddock paddock;
    private final RaceTrack raceTrack;
    private final ControlCenter controlCenter;
    private Logger logger;

   /**
   *    Constructor
   *
   *    @param name Horse/Jockey thread name
   *    @param horseJockeyID Horse/Jockey ID
   *    @param agility Horse agility
   */

   public HorseJockey (String name, int horseJockeyID, int agility, int raceId, Stable stable, Paddock paddock,
                            RaceTrack raceTrack, ControlCenter controlCenter, Logger logger)
   {
     super (name);
     this.id = horseJockeyID;
     this.raceId = raceId;
     this.agility = agility;
     this.state = HorseJockeyState.AT_THE_STABLE;
     this.stable = stable;
     this.paddock = paddock;
     this.raceTrack = raceTrack;
     this.controlCenter = controlCenter;
     this.logger = logger;
     logger.setHorseJockeyState(HorseJockeyState.AT_THE_STABLE, this.id, this.raceId);
     logger.setMaxMovingLength(this.agility, this.id, this.raceId);
   }

  /**
   *   Life cycle
   */

    @Override
    public void run ()
    {
        stable.proceedToStable();
        boolean last = paddock.proceedToPaddock();
        if(last)
        {                                                                                           // if is last horse to reach the paddock
            controlCenter.proceedToPaddock();              // call spectators
        }
        paddock.sleepAtThePaddock();                           // sleep (woken up by last last spectator to reach the paddock)

        paddock.proceedToStartLine();                           // if  is last horse to go to start line call spectators

        raceTrack.proceedToStartLine();                          // sleep (woken up by broker with startTheRace()
                                                                                                   //          or by another horse with makeAMove())
        boolean lastToCross = false;
        while(!raceTrack.hasRaceEnded())
        {
           lastToCross = raceTrack.makeAMove();         // sleep (woken up by previous horse)
        }

        if (lastToCross)
        {
            // other name would be best maybe
            controlCenter.makeAMove();
        }
        stable.proceedToEnd();                                       // sleep (final state)
    }

    /**
    *   Returns the state of the Horse/jockey pair
    *
    * @return current state of the Horse/Jockey pair
    */
    public HorseJockeyState getHorseJockeyState() {
        return this.state;
    }

    /**
    *   Updates the state of the Horse/jockey pair
    *
    * @param newState state to update Horse/Jockey pair to
    */
    public void setHorseJockeyState(HorseJockeyState newState)
    {
        this.state = newState;
    }

    public int getHorseJockeyID()
    {
        return id;
    }

    public int getAgility()
    {
        return agility;
    }

    public int getRaceId()
    {
        return raceId;
    }

    @Override
    public String toString()
    {
        return "HorseJockey{" +
                "id=" + id +
                ", agility=" + agility +
                ", raceId=" + raceId +
                ", state=" + state +
                '}';
    }
}