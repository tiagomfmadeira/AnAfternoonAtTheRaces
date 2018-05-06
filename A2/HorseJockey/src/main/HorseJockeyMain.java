package main;

import entities.HorseJockey;
import settings.Settings;
import stub.*;
import java.util.Random;
import static main.SimulPar.K_numRaces;
import static main.SimulPar.N_numCompetitors;

/**
 * General description: Main class.
 */
public class HorseJockeyMain
{

    /**
     * Creation and starting of the Horse/Jockey entity. Instantiation of the
     * required stubs.
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

        HorseJockey horseJockey[][] = new HorseJockey[K_numRaces][N_numCompetitors];
        Random rand = new Random();

        for (int i = 0; i < K_numRaces; i++)
        {
            for (int j = 0; j < N_numCompetitors; j++)
            {
                int race = i;
                int HJID = j;
                int agility = 1 + rand.nextInt(4);

                horseJockey[i][j] = new HorseJockey("horse_jockey_"
                        + HJID + "_race_" + race, HJID, agility, race,
                        stableStub, paddockStub, raceTrackStub, controlCenterStub, generalRepositoryStub);
                horseJockey[i][j].start();
            }
        }

        for (int i = 0; i < K_numRaces; i++)
        {
            for (int j = 0; j < N_numCompetitors; j++)
            {
                try
                {
                    horseJockey[i][j].join();
                } catch (InterruptedException e)
                {

                }
            }
        }
        generalRepositoryStub.shutdown();
    }
}
