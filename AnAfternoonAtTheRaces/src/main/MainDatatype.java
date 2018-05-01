package main;

import entities.Broker;
import entities.HorseJockey;
import entities.Spectator;
import sharedRegions.*;

import static main.SimulPar.K_numRaces;
import static main.SimulPar.M_numSpectators;
import static main.SimulPar.N_numCompetitors;

/**
 * General description: Main class.
 */
public class MainDatatype
{

    /**
     * Creation and starting of the various entity's threads (Broker, Spectators
     * and Horse/Jockey pairs). Instantiation of the shared memory regions.
     *
     * @param args command line arguments not used
     */
    public static void main(String[] args)
    {
        /*
        HorseJockey horseJockey[][] = new HorseJockey[K_numRaces][N_numCompetitors];
        Spectator spectator[] = new Spectator[M_numSpectators];


        GeneralRepository logger = new GeneralRepository();
        BettingCenter bettingCenter = new BettingCenter(logger);
        ControlCenter controlCenter = new ControlCenter(logger);
        Paddock paddock = new Paddock(logger);
        RaceTrack raceTrack = new RaceTrack(logger);
        Stable stable = new Stable(logger);

        for (int i = 0; i < M_numSpectators; i++)
        {
            int wallet = 1000;
            spectator[i] = new Spectator("spectator_" + i, i, wallet,
                    controlCenter, paddock, bettingCenter, logger);
            spectator[i].start();
        }

        for (int i = 0; i < K_numRaces; i++)
        {
            for (int j = 0; j < N_numCompetitors; j++)
            {
                int race = i;
                int HJID = j;
                int agility = 1 + rand.nextInt(4);

                horseJockey[i][j] = new HorseJockey("horse_jockey_"
                        + HJID + "_race_" + race, HJID, agility, race,
                        stable, paddock, raceTrack, controlCenter, logger);
                horseJockey[i][j].start();
            }
        }

        //Broker broker = new Broker("Broker", 1, raceTrack, stable, bettingCenter, paddock, controlCenter, logger);
        //broker.start();

        for (int i = 0; i < M_numSpectators; i++)
        {
            try
            {
                spectator[i].join();
            } catch (InterruptedException e)
            {

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

        try
        {
            broker.join();
        } catch (InterruptedException e)
        {

        }*/
    }
}
