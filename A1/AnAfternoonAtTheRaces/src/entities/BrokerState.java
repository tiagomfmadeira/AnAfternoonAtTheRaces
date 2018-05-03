package entities;

/**
 * General description: enum representing the Broker's possible states.
 */
public enum BrokerState
{
    OPENING_THE_EVENT("OTE"),
    ANNOUNCING_NEXT_RACE("ANR"),
    WAITING_FOR_BETS("WFB"),
    SUPERVISING_THE_RACE("STR"),
    SETTLING_ACCOUNTS("SA"),
    PLAYING_HOST_AT_THE_BAR("PHAB");

    private final String acronym;

    BrokerState(String acronym)
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
