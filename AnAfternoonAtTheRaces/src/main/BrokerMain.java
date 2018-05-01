package main;


import entities.Broker;
import settings.Settings;
import stub.*;
import static main.SimulPar.K_numRaces;
import static main.SimulPar.M_numSpectators;
import static main.SimulPar.N_numCompetitors;


public class BrokerMain {

    public static void main(String[] args)
    {

        GeneralRepositoryStub logger = new GeneralRepositoryStub(
            Settings.GENERAL_REPOSITORY.getHostName(),
            Settings.GENERAL_REPOSITORY.getPortNumber()
        );

        Settings settings = logger.getSettings(Settings.BETTING_CENTER.toString());

        System.out.println(settings.getHostName());
        System.out.println(settings.getPortNumber());


        //ControlCenterStub controlCenter = new ControlCenterStub(logger);
        //BettingCenterStub bettingCenter = new BettingCenterStub(logger);
        //PaddockStub paddock = new PaddockStub(logger);
        //RaceTrackStub raceTrack = new RaceTrackStub( logger);
        //StableStub stable = new StableStub(logger);


        //Broker broker = new Broker("Broker", 1, raceTrack, stable, bettingCenter, paddock, controlCenter, logger);
        //broker.start();
    }
}
