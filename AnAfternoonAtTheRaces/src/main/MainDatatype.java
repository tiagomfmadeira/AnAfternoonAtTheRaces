package main;

import entities.Broker;
import entities.HorseJockey;
import entities.Spectator;
import genclass.GenericIO;
import sharedRegions.*;

import java.util.Arrays;
import java.util.Random;

import static main.SimulPar.K_numRaces;
import static main.SimulPar.M_numSpectators;
import static main.SimulPar.N_numCompetitors;

public class MainDatatype
{
    public static void main(String [] args)
    {
        Random rand = new Random();
        int distance = +15 + rand.nextInt(10);

        //A bird told me shared regions shouldn't be static
        Logger logger = new Logger();

        BettingCenter bettingCenter = new BettingCenter(logger);
        ControlCenter controlCenter = new ControlCenter(logger);
        Paddock  paddock = new Paddock(logger);
        RaceTrack raceTrack = new RaceTrack(distance, logger);
        Stable stable = new Stable(logger);

        Spectator[ ] spectators = new Spectator[M_numSpectators];                                // array of producer threads
        HorseJockey[ ][ ]horseJockeyPairs = new HorseJockey[K_numRaces][N_numCompetitors];          // array of consumer threads

        Broker broker = new Broker("Broker",1, raceTrack , stable,
                                bettingCenter, paddock, controlCenter, logger );

        for (int i = 0; i < M_numSpectators; i++)
        {
            int wallet = rand.nextInt(1000)+4;
            spectators[ i ] = new Spectator("spectator_" + i, i, wallet, controlCenter, paddock, bettingCenter, logger);
            spectators[ i ].start ();
        }

        for (int i = 0; i < K_numRaces; i++)
        {
            for(int j = 0; j < N_numCompetitors; j++) {

                //HJID alone isn't enough to identify horse instance
                int race = i;
                int HJID = j;
                int agility = 1 + rand.nextInt(4);

                horseJockeyPairs[race][HJID] = new HorseJockey("horse_jockey_" + HJID + "_race_" + race , HJID, agility, race,
                                                                stable, paddock, raceTrack, controlCenter, logger);
                horseJockeyPairs[race][HJID].start();
            }
        }
        broker.start();
    }
}
