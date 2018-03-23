package entities;

public enum SpectatorState {
    WAITING_FOR_A_RACE_TO_START("WFR"),
    APPRAISING_THE_HORSES("ATH"),
    PLACING_A_BET("PAB"),
    WATCHING_A_RACE("WAR"),
    COLLECTING_THE_GAINS("CTG"),
    CELEBRATING("C");

    private final String acronym;

    SpectatorState(String acronym) {

        this.acronym = acronym;
    }

    public String getAcronym(){
        return this.acronym;
    }
}
