package sharedRegions;

import entities.*;
import main.SimulPar;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * General description: Definition of the General Repository of information.
 * Keeps a copy of the internal state of the problem and provides corresponding
 * logging, essential to the understanding of evolution of the system.
 */
public class Logger
{

    private BrokerState brokerState;
    private SpectatorState[] spectatorStates = new SpectatorState[SimulPar.M_numSpectators];
    private int[] moneyAmount = new int[SimulPar.M_numSpectators];
    private int[][] spectatorBetSelection = new int[SimulPar.K_numRaces][SimulPar.M_numSpectators];
    private int[][] spectatorBetAmount = new int[SimulPar.K_numRaces][SimulPar.M_numSpectators];
    private int raceNumber = 0;
    private int[] distanceInRace;
    private HorseJockeyState[][] horseJockeyState = new HorseJockeyState[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private double[][] horseOdds = new double[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private int[][] horseIteration = new int[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private int[][] horseAtEnd = new int[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private int[][] horsePosition = new int[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private int[][] maxMovingLength = new int[SimulPar.K_numRaces][SimulPar.N_numCompetitors];

    private String logFileName = "log";

    /**
     * Constructor
     */
    public Logger()
    {

        Arrays.fill(moneyAmount, -1);

        for (int[] row : spectatorBetSelection)
        {
            Arrays.fill(row, -1);
        }

        for (int[] row : spectatorBetAmount)
        {
            Arrays.fill(row, -1);
        }

        for (double[] row : horseOdds)
        {
            Arrays.fill(row, -1);
        }

        for (int[] row : horseIteration)
        {
            Arrays.fill(row, -1);
        }

        for (int[] row : horseAtEnd)
        {
            Arrays.fill(row, -1);
        }

        for (int[] row : horsePosition)
        {
            Arrays.fill(row, -1);
        }

        for (int[] row : maxMovingLength)
        {
            Arrays.fill(row, -1);
        }

        String headerLine1 = "MAN/BRK           SPECTATOR/BETTER              HORSE/JOCKEY PAIR at Race RN";

        String headerLine2 = "  Stat ";
        for (int i = 0; i < SimulPar.M_numSpectators; i++)
        {
            headerLine2 += " St" + i + "  " + "Am" + i;
        }
        headerLine2 += " RN";
        for (int i = 0; i < SimulPar.N_numCompetitors; i++)
        {
            headerLine2 += " St" + i + " " + "Len" + i;
        }

        String headerLine3 = String.format("%1$" + 45 + "s", "Race RN Status");

        String headerLine4 = " RN Dist";
        for (int i = 0; i < SimulPar.M_numSpectators; i++)
        {
            headerLine4 += " BS" + i + "  " + "BA" + i;
        }
        headerLine4 += " ";
        for (int i = 0; i < SimulPar.N_numCompetitors; i++)
        {
            headerLine4 += " Od" + i + " " + "N" + i + " " + "Ps" + i + " SD" + i;
        }

        try
        {
            FileWriter fw = new FileWriter(logFileName, false);
            fw.write(headerLine1 + "\n");
            fw.write(headerLine2 + "\n");
            fw.write(headerLine3 + "\n");
            fw.write(headerLine4 + "\n");
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Update the state of the Broker in log. Prints the internal state..
     *
     * @param brokerState state to update the Broker to
     */
    public synchronized void setBrokerState(BrokerState brokerState)
    {
        this.brokerState = brokerState;
        logState();
    }

    /**
     * Set the number of the race currently on the going to be logged and update
     * the Broker's state to ANNOUNCING_NEXT_RACE. Prints the internal state.
     *
     * @param raceNumber number of the current race to be logged
     */
    public synchronized void setRaceNumber(int raceNumber)
    {
        this.raceNumber = raceNumber;
        setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
    }

    /**
     * Set the length of the track in log, in which the races will occur, and
     * therefor the distance to be travelled each race.
     *
     * @param distanceInRace the length of the track
     */
    public synchronized void setDistanceInRace(int[] distanceInRace)
    {
        this.distanceInRace = distanceInRace;
    }

    /**
     * Update the amount of money a spectator holds in log.
     *
     * @param spectatorMoneyAmount value of money to update the wallet to in log
     * @param spectatorId          id of the spectator whose wallet is to be
     *                             updated in log.
     */
    public synchronized void setMoneyAmount(int spectatorMoneyAmount, int spectatorId)
    {
        this.moneyAmount[spectatorId] = spectatorMoneyAmount;
    }

    /**
     * Update the state of a spectator in log. Prints the internal state.
     *
     * @param spectatorState state to update the spectator in log to
     * @param spectatorId    id of the spectator to be updated in log
     */
    public synchronized void setSpectatorState(SpectatorState spectatorState, int spectatorId)
    {
        if (spectatorStates[spectatorId] != spectatorState)
        {
            this.spectatorStates[spectatorId] = spectatorState;
            logState();
        }
    }

    /**
     * Set the initial state of the Spectator in log. To be called upon creation
     * of respective thread. Useful to log initial state and wallet value in a
     * single operation.
     *
     * @param spectatorState initial state of the Spectator
     * @param spectatorId    id of the Spectator whose initial state is to be
     *                       logged
     * @param money          amount of money the Spectator started with
     */
    public synchronized void setSpectatorInitialState(SpectatorState spectatorState, int spectatorId, int money)
    {
        this.spectatorStates[spectatorId] = spectatorState;
        this.moneyAmount[spectatorId] = money;
    }

    /**
     * Set the information about a Spectator's bet in log.
     *
     * @param spectatorBetAmount    the value of the bet
     * @param spectatorBetSelection the horse id in which the Spectator is
     *                              betting their money
     * @param spectatorMoneyAmount  new balance of the Spectator's wallet
     * @param specId                id of the spectator
     */
    public synchronized void setSpectatorBet(int spectatorBetAmount, int spectatorBetSelection, int spectatorMoneyAmount, int specId)
    {
        this.spectatorBetAmount[this.raceNumber][specId] = spectatorBetAmount;
        this.spectatorBetSelection[this.raceNumber][specId] = spectatorBetSelection;
        this.moneyAmount[specId] = spectatorMoneyAmount;
    }

    /**
     * Update the state of a Horse/Jockey pair in log. Prints the internal
     * state.
     *
     * @param horseJockeyState state to update the horse/jockey pair to in log
     * @param horseJockeyId    id of the horse/jockey pair to be updated
     * @param raceId           id of the race the horse/jockey thread is
     *                         assigned to
     */
    public synchronized void setHorseJockeyState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId)
    {
        this.horseJockeyState[raceId][horseJockeyId] = horseJockeyState;
        if (raceId == this.raceNumber)
        {
            logState();
        }
    }

    /**
     * Set the initial state of a horse/jockey pair in log. To be called upon
     * creation of respective thread. Useful to log initial state and agility
     * value in a single operation. Only prints out the setting of horse/jockey
     * pairs belonging to the current race, for logging specification reasons.
     *
     * @param horseJockeyState initial state of the horse/jockey pair
     * @param horseJockeyId    id of the horse/jockey pair whose initial state
     *                         is to be set
     * @param raceId           id of the race the horse/jockey thread is
     *                         assigned to
     * @param agility          agility of of the horse/jockey
     */
    public synchronized void setHorseJockeyInitialState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId, int agility)
    {
        this.maxMovingLength[raceId][horseJockeyId] = agility;
        this.horseJockeyState[raceId][horseJockeyId] = horseJockeyState;

        boolean allAgilitySet = true;
        double totalAgility = 0;
        for (int i = 0; i < SimulPar.N_numCompetitors; i++)
        {
            if (this.maxMovingLength[raceId][i] == -1)
            {
                allAgilitySet = false;
                break;
            }
            totalAgility += this.maxMovingLength[raceId][i];
        }
        if (allAgilitySet == true)
        {
            for (int i = 0; i < SimulPar.N_numCompetitors; i++)
            {
                this.horseOdds[raceId][i] = this.maxMovingLength[raceId][i] / totalAgility;
            }
        }
    }

    /**
     * Update the movement of a horse/jockey pair during a race in log. Sets the
     * iteration and the distance travelled.
     *
     * @param horseIteration iteration the horse/jockey pair is currently in
     * @param horsePosition  distance the horse/jockey pair has travelled so far
     *                       in the race
     * @param horseId        id of the horse/jockey pair
     * @param raceId         id of the race the horse/jockey pair is assigned to
     */
    public synchronized void setHorseJockeyMove(int horseIteration, int horsePosition, int horseId, int raceId)
    {
        this.horseIteration[raceId][horseId] = horseIteration;
        this.horsePosition[raceId][horseId] = horsePosition;
    }

    /**
     * Set the stands of the Horse/Jockey pairs at the end of the race in log.
     *
     * @param horsesAtEnd finishing places (stands) of the Horse/Jockey pairs
     * @param raceId      id of the race the horse/jockey pair is assigned to
     */
    public synchronized void setHorseJockeyStands(int[] horsesAtEnd, int raceId)
    {
        this.horseAtEnd[raceId] = horsesAtEnd;
        logState();
    }

    /**
     * Prints out the internal state of the system in a formatted log.
     */
    private synchronized void logState()
    {
        // retrieve variables
        String line1 = "  " + String.format("%-4s", brokerState != null ? brokerState.getAcronym() : "####") + " ";

        for (int i = 0; i < SimulPar.M_numSpectators; i++)
        {
            SpectatorState s = spectatorStates[i];
            line1 += " " + String.format("%-3s", s != null ? s.getAcronym() : "###");

            line1 += " " + String.format("%-4" + (moneyAmount[i] != -1 ? "d" : "s"),
                    moneyAmount[i] != -1 ? moneyAmount[i] : "####");

        }

        line1 += "  " + String.format("%-1" + (brokerState == BrokerState.OPENING_THE_EVENT ? "s" : "d"),
                brokerState == BrokerState.OPENING_THE_EVENT ? "#" : this.raceNumber);

        if (brokerState == BrokerState.OPENING_THE_EVENT)
        {
            line1 += " ###  ##  ###  ##  ###  ##  ###  ## ";
        } else
        {
            for (int i = 0; i < SimulPar.N_numCompetitors; i++)
            {
                HorseJockeyState hj = horseJockeyState[this.raceNumber][i];

                int maxMovLen = maxMovingLength[this.raceNumber][i];

                line1 += " " + String.format("%-3s", hj != null ? hj.getAcronym() : "###");

                line1 += "  " + String.format("%-2" + (maxMovLen != -1 ? "d" : "s"),
                        maxMovLen != -1 ? maxMovLen : "##") + " ";
            }
        }

        ////////////////////////////////////////////////////////////////////////
        // Second Line
        String line2;

        if (brokerState == BrokerState.OPENING_THE_EVENT)
        {
            line2 = "  #  ##   #  ####  #  ####  #  ####  #  #### #### ##  ##  # #### ##  ##  # #### ##  ##  # #### ##  ##  #";
        } else
        {
            line2 = "  " + (this.raceNumber + 1) + "  " + this.distanceInRace[this.raceNumber] + " ";

            for (int i = 0; i < SimulPar.M_numSpectators; i++)
            {

                line2 += "  " + (spectatorBetSelection[this.raceNumber][i] != -1 ? spectatorBetSelection[this.raceNumber][i] : "#");

                int betAmt = spectatorBetAmount[this.raceNumber][i];

                line2 += "  " + String.format("%-4" + (betAmt != -1 ? "d" : "s"),
                        betAmt != -1 ? betAmt : "####");

            }

            for (int i = 0; i < SimulPar.N_numCompetitors; i++)
            {

                double odds = horseOdds[this.raceNumber][i];
                line2 += " " + String.format("%-4" + (odds != -1 ? ".2f" : "s"),
                        odds != -1 ? odds : "####");

                int horseIter = horseIteration[this.raceNumber][i];

                line2 += " " + String.format("%-2" + (horseIter != -1 ? "d" : "s"),
                        horseIter != -1 ? horseIter : "##");

                int horsePos = horsePosition[this.raceNumber][i];
                line2 += "  " + String.format("%-2" + (horsePos != -1 ? "d" : "s"),
                        horsePos != -1 ? horsePos : "##");

                line2 += "  " + (horseAtEnd[this.raceNumber][i] != -1 ? horseAtEnd[this.raceNumber][i] : "#");
            }
        }

        try
        {
            FileWriter fw = new FileWriter(logFileName, true);
            fw.write(line1 + "\n");
            fw.write(line2 + "\n");
            fw.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
