
import entities.HorseJockey;
import settings.Settings;
import stub.*;
import java.util.Random;

public class HorseJockeyMain
{

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

        HorseJockey horseJockey[][] = new HorseJockey[settings.K_numRaces][settings.N_numCompetitors];
        Random rand = new Random();

        for (int i = 0; i < settings.K_numRaces; i++)
        {
            for (int j = 0; j < settings.N_numCompetitors; j++)
            {
                int race = i;
                int HJID = j;
                int agility = 1 + rand.nextInt(4);

                horseJockey[i][j] = new HorseJockey("horse_jockey_"
                        + HJID + "_race_" + race, HJID, agility, race,
                        stable, paddock, raceTrack, controlCenter, gr);
                horseJockey[i][j].start();
            }
        }

        for (int i = 0; i < settings.K_numRaces; i++)
        {
            for (int j = 0; j < settings.N_numCompetitors; j++)
            {
                try
                {
                    horseJockey[i][j].join();
                } catch (InterruptedException e)
                {

                }
            }
        }
        gr.shutdown();
    }
}
