package serverSide.stable;

import genclass.GenericIO;
import interfaces.Register;
import settings.Settings;
import states.HorseJockeyState;
import interfaces.IGeneralRepository;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static settings.SimulPar.K_numRaces;
import static settings.SimulPar.N_numCompetitors;

/**
 * General description: Definition of the Stable information sharing region.
 */
public class Stable implements interfaces.IStable {

    /**
     * Internal data
     */
    // array of flags indexed per race
    private final boolean[] proceedToPaddockFlag = new boolean[K_numRaces];
    // counter of horses that left the paddock per race
    private final int[] proceededHorsesCount = new int[K_numRaces];
    private final IGeneralRepository logger;
    private boolean shutdownServer = false;

    /**
     * Constructor
     *
     * @param logger General Repository of information, keeping a copy of the
     *               internal state of the problem
     */
    public Stable(IGeneralRepository logger)
    {
        this.logger = logger;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Broker
    /**
     * Wake up the horse/jockey pairs assigned to a race from the AT_THE_STABLE
     * blocking state.
     *
     * @param raceID ID of the race that the horse/jockey pairs to be awaken are
     *               assigned to
     */
    @Override
    public synchronized void summonHorsesToPaddock(int raceID) throws RemoteException
    {

        proceedToPaddockFlag[raceID] = true;
        notifyAll();

        // a change state log here may be nec
        logger.setRaceNumber(raceID);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Horse/Jockey pair
    /**
     * Changes the horse/jockey pair state to AT_THE_STABLE and sleeps waiting
     * for a signal that the next race is starting.
     *
     * @param raceId the ID of the race
     */
    @Override
    public synchronized void proceedToStable(int raceId)
    {

        // check the flag for this race
        while (!proceedToPaddockFlag[raceId])
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }

        proceededHorsesCount[raceId]++;

        // if is last horse to leave stable
        if (proceededHorsesCount[raceId] == N_numCompetitors)
        {
            // reset var, the horses for this race have left
            proceedToPaddockFlag[raceId] = false;
        }
    }

    /**
     * Changes the horse/jockey pair state to AT_THE_STABLE. Used as a symbolic
     * return to the stable, allowing the thread to finish its life cycle
     * instead of blocking again. This is used because the thread would never be
     * awoken again.
     *
     * @param horseId the ID of the Horse?jockey pair
     * @param raceId  the ID of the race
     */
    @Override
    public synchronized void proceedToStableFinal(int horseId, int raceId) throws RemoteException
    {
        // change HorseJockey state to AT_THE_STABLE
        logger.setHorseJockeyState(HorseJockeyState.AT_THE_STABLE, horseId, raceId);
    }

    /**
     * Changes a boolean variable state to true, symbolising the conclusion of
     * the service.
     */
    @Override
    public synchronized void shutdown() throws RemoteException
    {
        Settings settings = logger.getSettings();

        String rmiRegHostName = Settings.REGISTRY_HOST_NAME;
        int rmiRegPortNumb = Settings.REGISTRY_PORT_NUM;

        String nameEntryBase = Settings.NAME_ENTRY_BASE;
        String nameEntryObject = settings.STABLE_NAME_ENTRY;

        Register reg = null;
        Registry registry = null;


        try
        {
            registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        {
            reg = (Register) registry.lookup (nameEntryBase);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RegisterRemoteObject lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("RegisterRemoteObject not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }


        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            GenericIO.writelnString ("RegisterRemoteObject unbind exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        } catch (NotBoundException e) {
            GenericIO.writelnString ("RegisterRemoteObject not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException e) {
            GenericIO.writelnString ("Stable unexport object exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        shutdownServer = true;
    }
    /**
     * Checks whether the service has been completed.
     *
     * @return <code>true</code> if the service has been completed
     *         <code>false</code> otherwise
     */
    @Override
    public synchronized boolean hasServiceFinished()
    {
        return shutdownServer;
    }
}
