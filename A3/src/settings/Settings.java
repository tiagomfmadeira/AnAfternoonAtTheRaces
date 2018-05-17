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
     * Registry host name
     */
    public static final String REGISTRY_HOST_NAME = "192.168.8.171";

    /*
     * Registry port number
     */
    public static final int REGISTRY_PORT_NUM = portNumb(0);

    /*
     * RegisterHandler name entry on the registry.
     */
    public static final String NAME_ENTRY_BASE = "RegisterHandler";

    /*
     * Register listening port.
     */
    public static final int REGISTER_PORT_NUM = portNumb(9);


    /*
     * General repository name entry on the registry.
     */
    public static final String GENERAL_REPOSITORY_NAME_ENTRY = "IGeneralRepository";

    /*
     * General repository listening port.
     */
    public static final int GENERAL_REPOSITORY_LISTENING_PORT = portNumb(1);


    /*
     * Betting center name entry on the registry.
     */
    public final String BETTING_CENTER_NAME_ENTRY = "IBettingCenter";

    /*
     * Betting center listening port.
     */
    public final int BETTING_CENTER_LISTENING_PORT = portNumb(2);


    /*
     * Control center name entry on the registry.
     */
    public final String CONTROL_CENTER_NAME_ENTRY = "IControlCenter";

    /*
     * Control center listening port.
     */
    public final int CONTROL_CENTER_LISTENING_PORT = portNumb(3);


    /*
     * Paddock name entry on the registry.
     */
    public final String PADDOCK_NAME_ENTRY = "IPaddock";

    /*
     * Paddock listening port.
     */
    public final int PADDOCK_LISTENING_PORT = portNumb(4);


    /*
     * Race track name entry on the registry.
     */
    public final String RACE_TRACK_NAME_ENTRY = "IRaceTrack";

    /*
     * Race track listening port.
     */
    public final int RACE_TRACK_LISTENING_PORT = portNumb(5);


    /*
     * Stable name entry on the registry.
     */
    public final String STABLE_NAME_ENTRY = "IStable";

    /*
     * Stable name listening port.
     */
    public final int STABLE_LISTENING_PORT = portNumb(6);







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
