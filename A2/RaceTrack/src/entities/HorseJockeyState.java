package entities;

/**
 * General description: enum representing the Horse/Jockey pair's possible
 * states.
 */
public enum HorseJockeyState
{
    AT_THE_STABLE("ATS"),
    AT_THE_PADDOCK("ATP"),
    AT_THE_START_LINE("ASL"),
    RUNNING("R"),
    AT_THE_FINNISH_LINE("ATF");

    private final String acronym;

    HorseJockeyState(String acronym)
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
