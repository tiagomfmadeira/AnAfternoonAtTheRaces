package entities;

/**
 * General description: enum representing the Spectator's possible states.
 */
public enum SpectatorState
{
    WAITING_FOR_A_RACE_TO_START("WFR"),
    APPRAISING_THE_HORSES("ATH"),
    PLACING_A_BET("PAB"),
    WATCHING_A_RACE("WAR"),
    COLLECTING_THE_GAINS("CTG"),
    CELEBRATING("C");

    private final String acronym;

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
