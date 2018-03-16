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

    private int id,                                             // Horse/Jockey thread ID
            agility,
            raceId;                                     // size of pace during race
    private HorseJockeyState state;                                 // Horse/Jockey state of the life cycle
    private final Stable stable;
    private final Paddock paddock;
    private final RaceTrack raceTrack;
    private final ControlCenter controlCenter;

   /**
   *    Constructor
   *
   *    @param name Horse/Jockey thread name
   *    @param HJID Horse/Jockey ID
   *    @param agility Horse agility
   */

   public HorseJockey(String name, int HJID, int agility, int raceId,
                      Stable stable, Paddock paddock, RaceTrack raceTrack, ControlCenter controlCenter)
   {
     super (name);
     this.id = HJID;
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
        {                                                                                     // if is last horse to go to paddock
            controlCenter.proceedToPaddock();          // call spectators
        }
        paddock.proceedToPaddock();                         // sleep (woken up by last last spectator to go see horses)

        last = paddock.proceedToStartLine();
        if(last)
        {                                                                                      // if  is last horse to go to start line
            paddock.lastProceedToStartLine();             // call spectators
        }
        raceTrack.proceedToStartLine();                     // sleep (woken up by broker with startTheRace()
                                                                                              //             or by another horse with makeAMove())
        do
        {
            raceTrack.makeAMove();                             // sleep (woken up by previous horse)
        } while(!raceTrack.hasFinishLineBeenCrossed());

        stable.proceedToStable();                               // sleep (final state)
    }

    public HorseJockeyState getHorseJockeyState() {
        return state;
    }

    public void setHorseJockeyState(HorseJockeyState state) {
        this.state = state;
    }

    public int getHJID() {
        return id;
    }

    public int getAgility() {
        return agility;
    }

    public int getRaceId() {
        return raceId;
    }

    @Override
    public String toString() {
        return "HorseJockey{" +
                "id=" + id +
                ", agility=" + agility +
                ", raceId=" + raceId +
                ", state=" + state +
                '}';
    }
}