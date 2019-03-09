package sharedRegions;

import entities.*;
import java.util.concurrent.ThreadLocalRandom;
import static main.SimulPar.M_numSpectators;
import static main.SimulPar.N_numCompetitors;

/**
 * General description: Definition of the Paddock information sharing region.
 */
public class Paddock
{

    /**
     * Internal data
     */
    private final HorseJockey[] horses = new HorseJockey[N_numCompetitors];
    private boolean lastSpectatorGoCheckHorses = false,
            lastHorseProceedToStartLine = false;
    private int horsesAtPaddockCount = 0,
            spectatorsAtPaddockCount = 0;
    private final Logger logger;

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
    //HorseJockey pair
    /**
     * Changes the state of the horse/jockey pair to AT_THE_PADDOCK and checks
     * whether or not it's the last pair to reach the paddock.
     *
     * @return <code>true</code> if it's called by the last horse to reach the
     *         paddock; <code>false</code> otherwise
     */
    public synchronized boolean proceedToPaddock()
    {
        boolean isLastHorse = false;

        // get ID of the horse/Jockey
        int horseJockeyID = ((HorseJockey) Thread.currentThread()).getHorseJockeyID();

        // save reference of the Horse/Jockey to be used by spectator thread in appraising
        horses[horseJockeyID] = (HorseJockey) Thread.currentThread();

        // change HorseJockey state to AT_THE_PADDOCK
        HorseJockey hj = (HorseJockey) Thread.currentThread();
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK);
        logger.setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK,
                hj.getHorseJockeyID(), hj.getRaceId());

        horsesAtPaddockCount++;

        // if is last horse to reach the paddock
        if (horsesAtPaddockCount == N_numCompetitors)
        {
            isLastHorse = true;
        }
        return isLastHorse;
    }

    /**
     * Sleep waiting for a signal that all the spectators have come to the
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

    ////////////////////////////////////////////////////////////////////////////
    //Spectator
    /**
     * Changes the Spectator's state to APPRAISING_THE_HORSES. Checks if it's
     * the last Spectator to reach the paddock to appraise the horses, in which
     * case it wakes up the horses sleeping at the paddock.
     *
     * @return <code>true</code> if it's called by the last Spectator to reach
     *         the paddock; <code>false</code> otherwise
     */
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
            isLastSpectator = true;
            lastSpectatorGoCheckHorses = true;
            notifyAll();
        }
        return isLastSpectator;
    }

    /**
     * Sleep waiting of a signal that the last Horse/Jockey pair has left the
     * paddock. Contains the logic for the betting choices of each spectator,
     * depending on their id. Spectator 0 bets on the Horse/Jockey pair with the
     * most agility; Spectator 1 bets on another Horse/Jockey pair, either with
     * the same agility as the best, or if there isn't such a Horse/Jockey pair,
     * on the second with most agility; Spectator 2 bets on the Horse/Jockey
     * pair with least agility; all other spectators bet randomly.
     *
     * @return id of the Horse/Jockey pair the Spectator will bet on
     */
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
            // reset, so spectators will block next race
            lastHorseProceedToStartLine = false;
        }

        // decide which Horse/Jockey pair to bet on
        int tmpHorseID = 0;
        int specId = ((Spectator) Thread.currentThread()).getSpectatorID();

        switch (specId)
        {
            // bet on the best Horse/Jockey pair
            case 0:
                for (int i = 0; i < N_numCompetitors; i++)
                {
                    if (horses[i].getAgility() > horses[tmpHorseID].getAgility())
                    {
                        tmpHorseID = i;
                    }
                }
                break;
            // bet on the second best Horse/Jockey pair
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
            // bet on the worst Horse/Jockey pair
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

    ////////////////////////////////////////////////////////////////////////////
    // Broker
    /**
     * Calculate the odds for each of the Horse/Jockey pairs assigned to the
     * current race.
     *
     * @return an array containing the odds for each of the Horse/Jockey pair of
     *         a race indexed by their id in it
     */
    public synchronized double[] learnTheOdds()
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
