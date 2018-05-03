package main;


import entities.Broker;
import settings.Settings;
import stub.*;


public class BrokerMain {

    public static void main(String[] args)
    {

        GeneralRepositoryStub gr = new GeneralRepositoryStub(
            Settings.GENERAL_REPOSITORY_HOST_NAME,
            Settings.GENERAL_REPOSITORY_PORT_NUM
        );

        Settings settings = gr.getSettings();

        ControlCenterStub controlCenter = new ControlCenterStub(
                settings.CONTROL_CENTER_HOST_NAME,
                settings.CONTROL_CENTER_PORT_NUM
        );

        BettingCenterStub bettingCenter = new BettingCenterStub(
                settings.BETTING_CENTER_HOST_NAME,
                settings.BETTING_CENTER_PORT_NUM
        );

        PaddockStub paddock = new PaddockStub(
                settings.PADDOCK_HOST_NAME,
                settings.PADDOCK_PORT_NUM
        );

        RaceTrackStub raceTrack = new RaceTrackStub(
                settings.RACE_TRACK_HOST_NAME,
                settings.RACE_TRACK_PORT_NUM
        );

        StableStub stable = new StableStub(
                settings.STABLE_HOST_NAME,
                settings.STABLE_PORT_NUM
        );


        Broker broker = new Broker("Broker", 1, raceTrack, stable, bettingCenter, paddock, controlCenter, gr);
        broker.start();

        try
        {
            broker.join();
        } catch (InterruptedException e)
        {}

        gr.shutdown();
    }
}
