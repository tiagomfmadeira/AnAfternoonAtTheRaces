package serverSide.paddock;

import genclass.GenericIO;
import interfaces.IBettingCenter;
import interfaces.IGeneralRepository;
import interfaces.IPaddock;
import interfaces.Register;
import serverSide.bettingCenter.BettingCenter;
import settings.Settings;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * General description: Main class.
 */
public class PaddockServer
{

    /**
     * Instantiation of the Paddock shared memory region. Setting up of the
     * server awaiting requests inbound. Instantiation of service providing
     * agents, which will execute functions of the shared memory region.
     *
     * @param args command line arguments not used
     */
    public static void main(String[] args)
    {
        String rmiRegHostName = Settings.REGISTRY_HOST_NAME;
        int rmiRegPortNumb = Settings.REGISTRY_PORT_NUM;

        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());
        GenericIO.writelnString ("Security manager was installed!");


        // get settings from general repository
        IGeneralRepository logger = null;
        Registry registry = null;

        try
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("RMI registry was created!");

        try
        { logger = (IGeneralRepository) registry.lookup (Settings.GENERAL_REPOSITORY_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("GeneralRepository lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("GeneralRepository not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        /* instanciação do objecto remoto que representa o playground e geração de um stub para ele */
        Paddock pd = new Paddock(logger);
        IPaddock pdStub = null;
        Settings settings = null;

        try
        {
            settings = logger.getSettings();
            int listeningPort = settings.PADDOCK_LISTENING_PORT;

            pdStub = (IPaddock) UnicastRemoteObject.exportObject (pd, listeningPort);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Paddock stub generation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("Paddock was generated!");


        String nameEntryBase = Settings.NAME_ENTRY_BASE;
        String nameEntryObject = settings.PADDOCK_NAME_ENTRY;
        Register reg = null;

        try
        { reg = (Register) registry.lookup (nameEntryBase);
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

        try
        { reg.bind (nameEntryObject, pdStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Paddock registration exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("Paddock already bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        GenericIO.writelnString ("Paddock object was registered!");

        pd.waitForShutSignal();

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
            UnicastRemoteObject.unexportObject(pd, true);
        } catch (NoSuchObjectException e) {
            GenericIO.writelnString ("Paddock unexport object exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
    }
}
