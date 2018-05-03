package entities;

import stub.*;

/**
 * General description: definition of the horse/jockey.
 */
public class HorseJockey extends Thread
{

    /**
     * Internal data
     */
    private final int id, // Horse/Jockey thread ID
            agility,
            raceId;                         // size of pace during race
    private HorseJockeyState state;         // Horse/Jockey state of the life cycle
    private final StableStub stable;
    private final PaddockStub paddock;
    private final RaceTrackStub raceTrack;
    private final ControlCenterStub controlCenter;

    /**
     * Constructor
     *
     * @param name          Horse/Jockey pair thread name
     * @param horseJockeyID Horse/Jockey pair ID
     * @param agility       Horse/Jockey pair agility, representation of the
     *                      maximum length of a step
     * @param raceId        Id of the race the Horse/Jockey pair is competing on
     * @param stable        Stable information sharing region
     * @param paddock       PaddockStub information sharing region
     * @param raceTrack     Race Track information sharing region
     * @param controlCenter Control Center/Watching Stand information sharing
     *                      region
     * @param logger        General Repository of information, keeping a copy of
     *                      the internal state of the problem
     */
    public HorseJockey(String name, int horseJockeyID, int agility, int raceId,
            StableStub stable, PaddockStub paddock, RaceTrackStub raceTrack,
            ControlCenterStub controlCenter, GeneralRepositoryStub logger)
    {
        super(name);
        this.id = horseJockeyID;
        this.raceId = raceId;
        this.agility = agility;
        this.state = HorseJockeyState.AT_THE_STABLE;
        this.stable = stable;
        this.paddock = paddock;
        this.raceTrack = raceTrack;
        this.controlCenter = controlCenter;
        logger.setHorseJockeyInitialState(HorseJockeyState.AT_THE_STABLE, horseJockeyID, raceId, agility);
    }

    /**
     * Life cycle
     */
    @Override
    public void run()
    {
        stable.proceedToStable();

        boolean last = paddock.proceedToPaddock();
        if (last)
        {                                                   // if is last Horse/Jockey pair to reach the paddock
            controlCenter.proceedToPaddock();               // call spectators
        }
        paddock.sleepAtThePaddock();                        // sleep (woken up by last last spectator to reach the paddock)

        paddock.proceedToStartLine();                       // if is last Horse/Jockey pair to go to start line call spectators

        raceTrack.proceedToStartLine();                     // sleep (woken up by broker with startTheRace() or by another Horse/Jockey pair with makeAMove())
        boolean lastToCross = false;
        while (!raceTrack.hasRaceEnded())
        {
            lastToCross = raceTrack.makeAMove();            // sleep (woken up by previous Horse/Jockey pair)
        }

        if (lastToCross)
        {
            controlCenter.makeAMove();
        }

        stable.proceedToStableFinal();                      // sleep (final state)
    }

    /**
     * Returns the current state of the Horse/Jockey pair.
     *
     * @return current state of the Horse/Jockey pair
     */
    public HorseJockeyState getHorseJockeyState()
    {
        return this.state;
    }

    /**
     * Updates the state of the Horse/Jockey pair.
     *
     * @param newState state to update Horse/Jockey pair to
     */
    public void setHorseJockeyState(HorseJockeyState newState)
    {
        this.state = newState;
    }

    /**
     * Returns the ID of the Horse/Jockey pair.
     *
     * @return ID of the Horse/Jockey pair
     */
    public int getHorseJockeyID()
    {
        return this.id;
    }

    /**
     * Returns the agility, e.g. the maximum length of a step, of the
     * Horse/Jockey pair.
     *
     * @return agility agility of the Horse/Jockey pair
     */
    public int getAgility()
    {
        return this.agility;
    }

    /**
     * Returns the ID of the race the Horse/Jockey pair participates in.
     *
     * @return id of the race the Horse/Jockey pair is assigned to
     */
    public int getRaceId()
    {
        return this.raceId;
    }

    /**
     * Returns a string representation of the Horse/Jockey pair.
     *
     * @return a string representation of the Horse/Jockey pair
     */
    @Override
    public String toString()
    {
        return "HorseJockey{"
                + "id=" + id
                + ", agility=" + agility
                + ", raceId=" + raceId
                + ", state=" + state
                + '}';
    }
}
