package main;

import entities.Broker;
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
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        {   GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { gr = (IGeneralRepository) registry.lookup (Settings.GENERAL_REPOSITORY_NAME_ENTRY);
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

        System.out.println(settings.BETTING_CENTER_NAME_ENTRY);
        System.out.println(settings.CONTROL_CENTER_NAME_ENTRY);
        System.out.println(settings.PADDOCK_NAME_ENTRY);

        /*
        ControlCenterStub controlCenterStub = new ControlCenterStub(
                settings.CONTROL_CENTER_HOST_NAME,
                settings.CONTROL_CENTER_PORT_NUM
        );

        BettingCenterStub bettingCenterStub = new BettingCenterStub(
                settings.BETTING_CENTER_HOST_NAME,
                settings.BETTING_CENTER_PORT_NUM
        );

        PaddockStub paddockStub = new PaddockStub(
                settings.PADDOCK_HOST_NAME,
                settings.PADDOCK_PORT_NUM
        );

        RaceTrackStub raceTrackStub = new RaceTrackStub(
                settings.RACE_TRACK_HOST_NAME,
                settings.RACE_TRACK_PORT_NUM
        );

        StableStub stableStub = new StableStub(
                settings.STABLE_HOST_NAME,
                settings.STABLE_PORT_NUM
        );

        Broker broker = new Broker("Broker", 1, raceTrackStub, stableStub, bettingCenterStub, paddockStub, controlCenterStub, generalRepositoryStub);
        broker.start();

        try
        {
            broker.join();
        } catch (InterruptedException e)
        {
        }

        generalRepositoryStub.shutdown();*/
    }
}
