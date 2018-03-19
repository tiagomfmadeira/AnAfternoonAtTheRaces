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

   /**
   *    Constructor
   *
   *    @param name Horse/Jockey thread name
   *    @param horseJockeyID Horse/Jockey ID
   *    @param agility Horse agility
   */

   public HorseJockey (String name, int horseJockeyID, int agility, int raceId,
                                                Stable stable, Paddock paddock, RaceTrack raceTrack, ControlCenter controlCenter)
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
   }

  /**
   *   Life cycle
   */

    @Override
    public void run ()
    {
        boolean last = stable.proceedToStable();
        if(last)
        {                                                                                           // if is last horse to go to paddock
            controlCenter.proceedToPaddock();              // call spectators
        }
        paddock.proceedToPaddock();                             // sleep (woken up by last last spectator to go see horses)

        paddock.proceedToStartLine();                           // if  is last horse to go to start line call spectators

        raceTrack.proceedToStartLine();                          // sleep (woken up by broker with startTheRace()
                                                                                                   //          or by another horse with makeAMove())
        do
        {
            raceTrack.makeAMove();                                      // sleep (woken up by previous horse)
        } while(!raceTrack.hasFinishLineBeenCrossed());

        stable.proceedToStable();                                       // sleep (final state)
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