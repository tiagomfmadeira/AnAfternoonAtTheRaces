package main;

import entities.Broker;
import settings.Settings;
import stub.*;

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

        GeneralRepositoryStub generalRepositoryStub = new GeneralRepositoryStub(
                Settings.GENERAL_REPOSITORY_HOST_NAME,
                Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = generalRepositoryStub.getSettings();

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

        generalRepositoryStub.shutdown();
    }
}
