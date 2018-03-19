package main;

import entities.Broker;
import entities.HorseJockey;
import entities.Spectator;
import genclass.GenericIO;
import sharedRegions.*;
import java.util.Random;

import static main.SimulPar.K_numRaces;
import static main.SimulPar.M_numSpectators;
import static main.SimulPar.N_numCompetitors;

public class MainDatatype
{

    public static void main(String [] args)
    {


        // static for now
        int distance = 20;


        //A bird told me shared regions shouldn't be static
        BettingCenter bettingCenter = new BettingCenter();
        ControlCenter controlCenter = new ControlCenter();
        Paddock  paddock = new Paddock();
        RaceTrack raceTrack = new RaceTrack(distance);
        Stable stable = new Stable();

        Spectator[] spectators = new Spectator[M_numSpectators];                                // array of producer threads
        HorseJockey[] horseJockeyPairs = new HorseJockey[K_numRaces*N_numCompetitors];          // array of consumer threads

        Broker broker = new Broker("Broker",1, raceTrack , stable,
                                bettingCenter, paddock, controlCenter );

        Random rand = new Random();
        for (int i = 0; i < M_numSpectators; i++)
        {
            int wallet = rand.nextInt(7)+2;
            spectators[i] = new Spectator("spectator_" + i, i, wallet, controlCenter, paddock, bettingCenter);
            spectators[i].start ();
            GenericIO.writelnString ("Current state of the spectator " + i + " thread is " + spectators[i].getState ().toString ());
        }

        for (int i = 0; i < K_numRaces; i++)
        {

            for(int j = 0; j < N_numCompetitors; j++) {

                //HJID alone isn't enough to identify horse instance
                int HJID = j;
                int agility = rand.nextInt(2)+1;

                horseJockeyPairs[HJID] = new HorseJockey("horse_jockey_" + i + "_race_" + j, j, HJID, agility,
                                                    stable, paddock, raceTrack, controlCenter);

                horseJockeyPairs[HJID].start();
                GenericIO.writelnString("Current state of the horseJockeyPairs " + i + " thread is " + horseJockeyPairs[i].getState().toString());
            }
        }

        broker.start();



    }
}
