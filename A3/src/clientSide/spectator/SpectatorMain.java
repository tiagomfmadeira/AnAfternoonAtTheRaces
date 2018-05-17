package clientSide.spectator;

import genclass.GenericIO;
import interfaces.*;
import settings.Settings;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static settings.SimulPar.M_numSpectators;

/**
 * General description: Main class.
 */
public class SpectatorMain
{

    /**
     * Creation and starting of the Spectator entity. Instantiation of the
     * required stubs.
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


        Spectator spectator[] = new Spectator[M_numSpectators];

        for (int i = 0; i < M_numSpectators; i++)
        {
            int wallet = 1000;
            spectator[i] = new Spectator("spectator_" + i, i, wallet,
                    cc, pd, bc, gr);
            spectator[i].start();
        }

        for (int i = 0; i < M_numSpectators; i++)
        {
            try
            {
                spectator[i].join();
            } catch (InterruptedException e)
            {
            }
        }

        //gr.shutdown();
    }
}
