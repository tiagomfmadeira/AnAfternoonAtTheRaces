package entities;
import main.SimulPar;
import sharedRegions.*;

import static main.SimulPar.K_numRaces;

/**
 *  General description:
 *      definition of the broker..
 */

public class Broker extends Thread
{

    /**
   *    Internal data
   */

    private int id,                              // Broker thread ID
         numRaces;                               // number of races
    private BrokerState state;                   // Broker state of the life cycle
    private Stable stable;
    private ControlCenter controlCenter;
    private Paddock paddock;
    private BettingCenter bettingCenter;
    private RaceTrack raceTrack;

   /**
   *    Constructor
   *
   *    @param name broker thread name
   *    @param brokerID broker ID
   *
   */

   // Regions are passed in constructor for now
   public Broker (String name, int brokerID, RaceTrack race_track, Stable stable,
                  BettingCenter bettingCenter, Paddock paddock, ControlCenter controlCenter)
   {
     super (name);
     this.id = brokerID;
     this.numRaces = SimulPar.K_numRaces;
     this.state = BrokerState.OPENING_THE_EVENT;
     this.controlCenter = controlCenter;
     this.paddock = paddock;
     this.bettingCenter = bettingCenter;
     this.stable = stable;
     this.raceTrack = race_track;
   }

  /**
   *   Life cycle
   */

   @Override
    public void run ()
    {
        for(int k = 0; k < numRaces; k++)                                            // for each race
        {

            stable.summonHorsesToPaddock(k);                 // call the horses for a race
            paddock.summonHorsesToPaddock();                 // sleep (woken up by last last spectator to go see horses)

            controlCenter.acceptTheBets();                           // sleep (woken up by each spectator to place  bet
                                                                                                        // transition occurs when all have placed bets)

            raceTrack.startTheRace();                                     // call horse/jockey pairs
            controlCenter.startTheRace();                              // sleep (woken up by last horse to cross finish line)

            controlCenter.reportResults();                              // call the spectators

            if(bettingCenter.areThereAnyWinners())
            {
                bettingCenter.honourTheBets();                       // sleep (woken up by each winner
                                                                                                        // transition occurs when all winner have been paid)
            }
        }
        controlCenter.entertainTheGuests();                      //sleep (final state)
    }


    public BrokerState getBorkerState() {
        return state;
    }

    public void setBrokerState(BrokerState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Broker{" +
                "id=" + id +
                ", numRaces=" + numRaces +
                ", state=" + state +
                '}';
    }
}
