package sharedRegions;

import entities.*;
import java.util.concurrent.ThreadLocalRandom;

import static main.SimulPar.M_numSpectators;
import static main.SimulPar.N_numCompetitors;

public class Paddock
{

    private HorseJockey[] horses = new HorseJockey[N_numCompetitors];
    private boolean lastSpectatorGoCheckHorses = false,
            lastHorseProceedToStartLine = false;
    private int horsesAtPaddockCount = 0,
            spectatorsAtPaddockCount = 0;
    private Logger logger;

    /**
     * Constructor
     *
     * @param logger General Repository of information, keeping a copy of the
     *               internal state of the problem
     */
    public Paddock(Logger logger)
    {
        this.logger = logger;
    }

    ////////////////////////////////////////////////////////////////////////////
    //HorseJockey
    /**
     * Changes the state of the horse/jockey pair to AT_THE_PADDOCK and returns
     * whether or not it's the last pair to reach the paddock.
     *
     * @return <code>true</code> if it's called by the last horse to reach the
     *         paddock; <code>false</code> otherwise
     */
    public synchronized boolean proceedToPaddock()
    {
        boolean isLastHorse = false;

        // Get ID of the horse/Jockey
        int horseJockeyID = ((HorseJockey) Thread.currentThread()).getHorseJockeyID();

        // Save reference of the Horse/Jockey to be used by spectator thread in appraising
        horses[horseJockeyID] = (HorseJockey) Thread.currentThread();
        int raceId = ((HorseJockey) Thread.currentThread()).getRaceId();
        int agility = ((HorseJockey) Thread.currentThread()).getAgility();
        logger.setMaxMovingLength(agility, horseJockeyID, raceId);

        //  Change HorseJockey state to AT_THE_PADDOCK
        HorseJockey hj = (HorseJockey) Thread.currentThread();
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK);
        logger.setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK,
                hj.getHorseJockeyID(), hj.getRaceId());

        horsesAtPaddockCount++;

        if (horsesAtPaddockCount == N_numCompetitors)
        {
            isLastHorse = true;
        }
        return isLastHorse;
    }

    /**
     * Sleeps waiting for a signal that all the spectators have come to the
     * paddock to appraise the horses.
     */
    public synchronized void sleepAtThePaddock()
    {
        // Wait for all the spectators to check the horses
        while (!lastSpectatorGoCheckHorses)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }
    }

    /**
     * Checks whether it's the last horse/jockey pair to leave the paddock and
     * head to the start line, in which case it wakes up the spectators.
     */
    public synchronized void proceedToStartLine()
    {

        horsesAtPaddockCount--;

        // if is last horse to leave paddock
        if (horsesAtPaddockCount == 0)
        {
            lastHorseProceedToStartLine = true;
            notifyAll();

            // reset var, so next time horses come to paddock they block
            lastSpectatorGoCheckHorses = false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Spectator
    public synchronized boolean goCheckHorses()
    {
        boolean isLastSpectator = false;

        //  Change Spectator state to APPRAISING_THE_HORSES
        ((Spectator) Thread.currentThread()).setSpectatorState(SpectatorState.APPRAISING_THE_HORSES);
        logger.setSpectatorState(SpectatorState.APPRAISING_THE_HORSES,
                ((Spectator) Thread.currentThread()).getSpectatorID());

        spectatorsAtPaddockCount++;

        if (spectatorsAtPaddockCount == M_numSpectators)
        {
            // return var
            isLastSpectator = true;
            notifyAll();
        }
        return isLastSpectator;
    }

    public synchronized int appraisingHorses()
    {
        while (!lastHorseProceedToStartLine)
        {
            try
            {
                wait();
            } catch (InterruptedException e)
            {

            }
        }

        spectatorsAtPaddockCount--;

        // if is last spectator to leave
        if (spectatorsAtPaddockCount == 0)
        {
            // reset , so spectators will block next race
            lastHorseProceedToStartLine = false;
        }

        //TODO: further logic should be implemented
        // Decide which horse to bet on
        int tmpHorseID = 0;
        int specId = ((Spectator) Thread.currentThread()).getSpectatorID();

        switch (specId)
        {
            // bet on the best horse
            case 0:
                for (int i = 0; i < N_numCompetitors; i++)
                {
                    if (horses[i].getAgility() > horses[tmpHorseID].getAgility())
                    {
                        tmpHorseID = i;
                    }
                }
                break;
            // bet on the second best horse
            case 1:
                int high1 = Integer.MIN_VALUE;
                int high2 = Integer.MIN_VALUE;
                for (int i = 0; i < N_numCompetitors; i++)
                {
                    int tmpAg = horses[i].getAgility();
                    if (tmpAg > high1)
                    {
                        high2 = high1;
                        high1 = tmpAg;
                    } else if (tmpAg > high2)
                    {
                        high2 = tmpAg;
                    }
                }
                for (int i = 0; i < N_numCompetitors; i++)
                {
                    if (horses[i].getAgility() == high2)
                    {
                        tmpHorseID = i;
                    }
                }
                break;
            // bet on the worst horse
            case 2:
                for (int i = 0; i < N_numCompetitors; i++)
                {
                    if (horses[i].getAgility() < horses[tmpHorseID].getAgility())
                    {
                        tmpHorseID = i;
                    }
                }
                break;
            // bet on anything
            default:
                tmpHorseID = ThreadLocalRandom.current().nextInt(N_numCompetitors);
                break;
        }

        return tmpHorseID;
    }

    public synchronized void lastToCheckHorses()
    {
        lastSpectatorGoCheckHorses = true;
        notifyAll();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Broker
    public synchronized double[] acceptTheBets()
    {
        double[] odds = new double[N_numCompetitors];
        double totalAgility = 0;

        // get the total agility
        for (int i = 0; i < N_numCompetitors; i++)
        {
            totalAgility += horses[i].getAgility();
        }

        // calculate the odds of each horse
        for (int i = 0; i < N_numCompetitors; i++)
        {
            odds[i] = horses[i].getAgility() / totalAgility;
        }

        return odds;
    }
}
