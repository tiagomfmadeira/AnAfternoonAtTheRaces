package entities;

public enum HorseJockeyState {
    AT_THE_STABLE("ATS"),
    AT_THE_PADDOCK("ATP"),
    AT_THE_START_LINE("ASL"),
    RUNNING("R"),
    AT_THE_FINNISH_LINE("ATF");

    private final String acronym;

    HorseJockeyState(String acronym) {

        this.acronym = acronym;
    }

    public String getAcronym(){
        return  this.acronym;
    }
}
