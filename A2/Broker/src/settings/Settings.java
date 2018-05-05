package settings;

import java.io.Serializable;

/**
 * General description: Definition of address settings of the servers.
 */
public class Settings implements Serializable
{

    private static Settings instance = null;

    private Settings()
    {
    }

    /*
     * General repository server host name
     */
    public static final String GENERAL_REPOSITORY_HOST_NAME = "localhost";

    /*
     * General repository server port number
     */
    public static final int GENERAL_REPOSITORY_PORT_NUM = portNumb(1);

    /*
     * Betting Center server host name
     */
    public final String BETTING_CENTER_HOST_NAME = "localhost";

    /*
     * Betting Center server port number
     */
    public final int BETTING_CENTER_PORT_NUM = portNumb(2);

    /*
     * Control Center server host name
     */
    public final String CONTROL_CENTER_HOST_NAME = "localhost";
    /*
     * Control Center server port number
     */
    public final int CONTROL_CENTER_PORT_NUM = portNumb(3);

    /*
     * Paddock server host name
     */
    public final String PADDOCK_HOST_NAME = "localhost";
    /*
     * Paddock server server port number
     */
    public final int PADDOCK_PORT_NUM = portNumb(4);

    /*
     * Race Track server host name
     */
    public final String RACE_TRACK_HOST_NAME = "localhost";
    /*
     * Race Track server port number
     */
    public final int RACE_TRACK_PORT_NUM = portNumb(5);

    /*
     * Stable server host name
     */
    public final String STABLE_HOST_NAME = "localhost";
    /*
     * Stable server port number
     */
    public final int STABLE_PORT_NUM = portNumb(6);

    /**
     * Provides the port number calculated based on the provided relative port
     * number and certain restrictions.
     *
     * @param port the relative port number
     *
     * @return the port number calculated
     */
    private static int portNumb(int port)
    {
        return 22450 + port;
    }

    /**
     * Provides an instance of the settings
     *
     * @return an instance of the settings
     */
    public static Settings getInstance()
    {
        if (instance == null)
        {
            instance = new Settings();
        }
        return instance;
    }
}
