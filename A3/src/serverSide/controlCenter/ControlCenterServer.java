package serverSide.controlCenter;

import genclass.GenericIO;
import interfaces.IControlCenter;
import interfaces.IGeneralRepository;
import interfaces.Register;
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
public class ControlCenterServer
{

    /**
     * Instantiation of the Control Center shared memory region. Setting up of
     * the server awaiting requests inbound. Instantiation of service providing
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
        ControlCenter cc = new ControlCenter(logger);
        IControlCenter ccStub = null;
        Settings settings = null;

        try
        {
            settings = logger.getSettings();
            int listeningPort = settings.CONTROL_CENTER_LISTENING_PORT;

            ccStub = (IControlCenter) UnicastRemoteObject.exportObject (cc, listeningPort);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ControlCenter stub generation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("ControlCenter was generated!");


        String nameEntryBase = Settings.NAME_ENTRY_BASE;
        String nameEntryObject = settings.CONTROL_CENTER_NAME_ENTRY;
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
        { reg.bind (nameEntryObject, ccStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ControlCenter registration exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("ControlCenter already bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        GenericIO.writelnString ("ControlCenter object was registered!");

        cc.waitForShutSignal();

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
            UnicastRemoteObject.unexportObject(cc, true);
        } catch (NoSuchObjectException e) {
            GenericIO.writelnString ("ControlCenter unexport object exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
    }
}
