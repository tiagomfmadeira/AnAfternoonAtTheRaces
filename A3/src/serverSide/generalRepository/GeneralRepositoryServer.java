package serverSide.generalRepository;

import genclass.GenericIO;
import interfaces.Register;
import settings.Settings;
import interfaces.IGeneralRepository;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * General description: Main class.
 */
public class GeneralRepositoryServer
{

    /**
     * Instantiation of the General Repository shared memory region. Setting up
     * of the server awaiting requests inbound. Instantiation of service
     * providing agents, which will execute functions of the shared memory
     * region.
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

        GeneralRepository gr = new GeneralRepository();
        IGeneralRepository grStub = null;
        int listeningPort = Settings.GENERAL_REPOSITORY_LISTENING_PORT;

        try
        { grStub = (IGeneralRepository) UnicastRemoteObject.exportObject (gr, listeningPort);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("General Repository stub generation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("General Repository was generated!");

        String nameEntryBase = Settings.NAME_ENTRY_BASE;
        String nameEntryObject = Settings.GENERAL_REPOSITORY_NAME_ENTRY;
        Registry registry = null;
        Register reg = null;

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
        { reg.bind (nameEntryObject, grStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("General Repository registration exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("General Repository already bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        GenericIO.writelnString ("General Repository object was registered!");

    }
}
