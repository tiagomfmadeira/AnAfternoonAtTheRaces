package sharedRegions;

import entities.HorseJockey;
import entities.HorseJockeyState;
import static main.SimulPar.K_numRaces;
import static main.SimulPar.N_numCompetitors;


public class Stable
{
    // array of flags indexed per race
    private boolean[ ] proceedToPaddockFlag = new boolean[K_numRaces];
    // counter of horses that left the paddock per race
    private int[ ] proceededHorsesCount = new int[K_numRaces];
    private boolean nextRaceExists = true;

    private Logger logger;

    public Stable(Logger logger){
        this.logger = logger;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Broker

    public synchronized void summonHorsesToPaddock(int raceID)
    {
        logger.setRaceNumber(raceID);
        proceedToPaddockFlag[raceID] = true;
        notifyAll();
    }

    public synchronized void entertainTheGuests()
    {
        nextRaceExists = false;
        notifyAll();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Horse/Jockey

    public synchronized void proceedToStable()
    {
        //  Change HorseJockey state to AT_THE_STABLE
        HorseJockey hj = ((HorseJockey) Thread.currentThread());
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_STABLE);
        int horseId = hj.getHorseJockeyID();
        int raceId = hj.getRaceId();

        logger.setHorseJockeyState(HorseJockeyState.AT_THE_STABLE, horseId, raceId );
        logger.setMaxMovingLength(hj.getAgility(), horseId, raceId);


        // Get race ID
        int raceID = ((HorseJockey) Thread.currentThread()).getRaceId();

        // Check the flag for this race
        while (!proceedToPaddockFlag[raceID] && nextRaceExists)
        {
            try
            {
                wait ();
            }
            catch (InterruptedException e)
            {

            }
        }

        proceededHorsesCount[raceID]++;

        if (proceededHorsesCount[raceID] ==N_numCompetitors)        // last horse to leave stable
        {
            // reset var, the horses for this race have left
            proceedToPaddockFlag[raceID] = false;
        }
    }
}
