package states;

/**
 * General description: enum representing the Spectator's possible states.
 */
public enum SpectatorState
{
    /**
     * Represents the state of the Spectator Waiting For a Race to start
     */
    WAITING_FOR_A_RACE_TO_START("WFR"),
    /**
     * Represents the state of the Spectator Appraising The Horses
     */
    APPRAISING_THE_HORSES("ATH"),
    /**
     * Represents the state of the Spectator Placing A Bet
     */
    PLACING_A_BET("PAB"),
    /**
     * Represents the state of the Spectator Watching A Race
     */
    WATCHING_A_RACE("WAR"),
    /**
     * Represents the state of the Spectator Collecting The Gains
     */
    COLLECTING_THE_GAINS("CTG"),
    /**
     * Represents the state of the Spectator Celebrating
     */
    CELEBRATING("C");

    private final String acronym;

    /**
     * Constructor
     *
     * @param acronym the acronym of the state
     */
    SpectatorState(String acronym)
    {

        this.acronym = acronym;
    }

    /**
     * Provides the acronym for the enum.
     *
     * @return the acronym representing a state
     */
    public String getAcronym()
    {
        return this.acronym;
    }
}
