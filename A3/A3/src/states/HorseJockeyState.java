package states;

/**
 * General description: enum representing the Horse/Jockey pair's possible
 * states.
 */
public enum HorseJockeyState
{
    /**
     * Represents the state of the Horse/Jockey At the Stable
     */
    AT_THE_STABLE("ATS"),
    /**
     * Represents the state of the Horse/Jockey At The Paddock
     */
    AT_THE_PADDOCK("ATP"),
    /**
     * Represents the state of the Horse/Jockey At the Start Line
     */
    AT_THE_START_LINE("ASL"),
    /**
     * Represents the state of the Horse/Jockey Running
     */
    RUNNING("R"),
    /**
     * Represents the state of the Horse/Jockey At The Finish line
     */
    AT_THE_FINNISH_LINE("ATF");

    private final String acronym;

    /**
     * Constructor
     *
     * @param acronym the acronym of the state
     */
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
