package settings;

public enum Settings {

    GENERAL_REPOSITORY("localhost", portNumb(1)),
    BETTING_CENTER("localhost", portNumb(2)),
    CONTROL_CENTER("localhost", portNumb(3)),
    PADDOCK("localhost", portNumb(4)),
    RACE_TRACK("localhost", portNumb(5)),
    STABLE("localhost", portNumb(6));


    /**
     * Number of races to occur
     */
    public static final int K_numRaces = 5;

    /**
     * Number of Horse/Jockey pairs to compete in the races
     */
    public static final int N_numCompetitors = 4;

    /**
     * Number of Spectators
     */
    public static final int M_numSpectators = 4;


    private final int portNumber;
    private final String hostName;


    Settings(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    private static int portNumb(int workstation){
        return 2650+workstation;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getHostName() {
        return hostName;
    }
}
