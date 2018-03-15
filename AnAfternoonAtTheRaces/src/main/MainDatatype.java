package main;

import entities.Broker;
import entities.HorseJockey;
import entities.Spectator;
import genclass.GenericIO;
import sharedRegions.*;
;import java.util.Random;

public class MainDatatype
{
    public static void main(String [] args)
    {
        // static for now
        int K_numRaces = 5;
        int N_numCompetitors = 4;
        int M_numSpectators = 4;
        int distance = 20;


        //A bird told me shared regions shouldn't be static
        BettingCenter bettingCenter = new BettingCenter();
        ControlCenter controlCenter = new ControlCenter();
        Paddock  paddock = new Paddock();
        RaceTrack raceTrack = new RaceTrack();
        Stable stable = new Stable();

        Spectator[] spectators = new Spectator[M_numSpectators];                                // array of producer threads
        HorseJockey[] horseJockeyPairs = new HorseJockey[N_numCompetitors*K_numRaces];          // array of consumer threads

        Broker broker = new Broker("Broker",1, K_numRaces , raceTrack , stable,
                                bettingCenter, paddock, controlCenter );

        for (int i = 0; i < M_numSpectators; i++)
        {
            spectators[i] = new Spectator("spectator_" + i,i,controlCenter, paddock, bettingCenter);
            spectators[i].start ();
            GenericIO.writelnString ("Current state of the spectator " + i + " thread is " + spectators[i].getState ().toString ());
        }

        Random rand = new Random();
        for (int i = 0; i < N_numCompetitors; i++)
        {
            for(int j = 0; j < K_numRaces; j++) {

                int HJID = i*K_numRaces+j;
                int agility = rand.nextInt(2)+1;

                horseJockeyPairs[HJID] = new HorseJockey("horse_jockey_" + i + "_race_" + j, HJID, agility,
                                                    stable, paddock, raceTrack, controlCenter);

                horseJockeyPairs[HJID].start();
                GenericIO.writelnString("Current state of the horseJockeyPairs " + i + " thread is " + horseJockeyPairs[i].getState().toString());
            }
        }

        broker.start();



    }
}
