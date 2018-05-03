package settings;

import java.io.Serializable;

public class Settings implements Serializable
{

    private static Settings instance = null;

    private Settings()
    {
    }

    // settings accessed by all
    public static final String GENERAL_REPOSITORY_HOST_NAME = "localhost";
    public static final int GENERAL_REPOSITORY_PORT_NUM = portNumb(1);

    // these fields should only be accessed by the generalRepositoryServer
    public final String BETTING_CENTER_HOST_NAME = "localhost";
    public final int BETTING_CENTER_PORT_NUM = portNumb(2);

    public final String CONTROL_CENTER_HOST_NAME = "localhost";
    public final int CONTROL_CENTER_PORT_NUM = portNumb(3);

    public final String PADDOCK_HOST_NAME = "localhost";
    public final int PADDOCK_PORT_NUM = portNumb(4);

    public final String RACE_TRACK_HOST_NAME = "localhost";
    public final int RACE_TRACK_PORT_NUM = portNumb(5);

    public final String STABLE_HOST_NAME = "localhost";
    public final int STABLE_PORT_NUM = portNumb(6);

    /**
     * Number of races to occur
     */
    public final int K_numRaces = 5;

    /**
     * Number of Horse/Jockey pairs to compete in the races
     */
    public final int N_numCompetitors = 4;

    /**
     * Number of Spectators
     */
    public final int M_numSpectators = 4;

    private static int portNumb(int port)
    {
        return 22450 + port;
    }

    public static Settings getInstance()
    {
        if (instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }

}
