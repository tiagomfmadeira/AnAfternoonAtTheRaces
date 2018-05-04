package entities;

/**
 * General description: enum representing the Broker's possible states.
 */
public enum BrokerState
{
    /**
     * Represents the state of the Broker Opening The Event
     */
    OPENING_THE_EVENT("OTE"),
    /**
     * Represents the state of the Broker Announcing Next Race
     */
    ANNOUNCING_NEXT_RACE("ANR"),
    /**
     * Represents the state of the Broker Waiting For Bets
     */
    WAITING_FOR_BETS("WFB"),
    /**
     * Represents the state of the Broker Supervising The Race
     */
    SUPERVISING_THE_RACE("STR"),
    /**
     * Represents the state of the Broker Setting Accounts
     */
    SETTLING_ACCOUNTS("SA"),
    /**
     * Represents the state of the Broker Playing Host At The Bar
     */
    PLAYING_HOST_AT_THE_BAR("PHAB");

    private final String acronym;

    /**
     * Constructor
     *
     * @param acronym the acronym of the state
     */
    BrokerState(String acronym)
    {
        this.acronym = acronym;
    }

    /**
     * Returns the acronym for the enum.
     *
     * @return the acronym representing a state
     */
    public String getAcronym()
    {
        return this.acronym;
    }
}
