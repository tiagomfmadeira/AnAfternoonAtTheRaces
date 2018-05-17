package clientSide.broker;

import genclass.GenericIO;
import interfaces.*;
import settings.Settings;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * General description: Main class.
 */
public class BrokerMain
{

    /**
     * Creation and starting of the Broker entity. Instantiation of the required
     * stubs.
     *
     * @param args command line arguments not used
     */
    public static void main(String[] args)
    {


        String rmiRegHostName = Settings.REGISTRY_HOST_NAME;
        int rmiRegPortNumb = Settings.REGISTRY_PORT_NUM;

        IGeneralRepository gr = null;
        IControlCenter cc = null;
        IBettingCenter bc = null;
        IPaddock pd = null;
        IRaceTrack rt = null;
        IStable st = null;
        Registry registry = null;

        Settings settings = null;

        try
        {
            registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        {
            gr = (IGeneralRepository) registry.lookup (Settings.GENERAL_REPOSITORY_NAME_ENTRY);
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("GeneralRepository look up exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("GeneralRepository not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        {
            settings = gr.getSettings();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("GeneralRepository remote invocation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        {
            cc = (IControlCenter) registry.lookup (settings.CONTROL_CENTER_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ControlCenter look up exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("ControlCenter not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        {
            bc = (IBettingCenter) registry.lookup (settings.BETTING_CENTER_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("BettingCenter look up exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("BettingCenter not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        {
            pd = (IPaddock) registry.lookup (settings.PADDOCK_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Paddock look up exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Paddock not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        {
            rt = (IRaceTrack) registry.lookup (settings.RACE_TRACK_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Paddock look up exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Paddock not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        {
            st = (IStable) registry.lookup (settings.STABLE_NAME_ENTRY);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Stable look up exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Stable not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }


        Broker broker = new Broker("Broker", 1, rt, st, bc, pd, cc, gr);
        broker.start();

        try
        {
            broker.join();
        } catch (InterruptedException e)
        {
        }

        //gr.shutdown();
    }
}
