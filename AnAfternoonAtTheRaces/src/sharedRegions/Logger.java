package sharedRegions;

import entities.*;
import genclass.FileOp;
import genclass.GenericIO;
import genclass.TextFile;
import main.SimulPar;

import java.util.Arrays;

import static main.SimulPar.N_numCompetitors;

public class Logger
{

    private BrokerState brokerState = BrokerState.OPENING_THE_EVENT;
    private SpectatorState[] spectatorStates = new SpectatorState[SimulPar.M_numSpectators];
    private int[] moneyAmount = new int[SimulPar.M_numSpectators];
    private int[][] spectatorBetSelection = new int[SimulPar.K_numRaces][SimulPar.M_numSpectators];
    private int[][] spectatorBetAmount = new int[SimulPar.K_numRaces][SimulPar.M_numSpectators];

    private int raceNumber = 0;//broker
    //private int[] distanceInRace = new int[SimulPar.K_numRaces];
    private int distanceInRace;

    private HorseJockeyState[][] horseJockeyState = new HorseJockeyState[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private double[][] horseOdds = new double[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private int[][] horseIteration = new int[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private boolean[][] horseAtEnd = new boolean[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private int[][] horsePosition = new int[SimulPar.K_numRaces][SimulPar.N_numCompetitors];
    private int[][] maxMovingLength = new int[SimulPar.K_numRaces][SimulPar.N_numCompetitors];

    public Logger()
    {

        Arrays.fill(moneyAmount, -1);

        Arrays.fill(spectatorStates, SpectatorState.WAITING_FOR_A_RACE_TO_START);

        for (int[] row : spectatorBetSelection)
        {
            Arrays.fill(row, -1);
        }

        for (int[] row : spectatorBetAmount)
        {
            Arrays.fill(row, -1);
        }

        for (HorseJockeyState[] row : horseJockeyState)
        {
            Arrays.fill(row, HorseJockeyState.AT_THE_STABLE);
        }

        for (double[] row : horseOdds)
        {
            Arrays.fill(row, -1);
        }

        for (int[] row : horseIteration)
        {
            Arrays.fill(row, -1);
        }

        for (boolean[] row : horseAtEnd)
        {
            Arrays.fill(row, false);
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

        GenericIO.writelnString(headerLine1);
        GenericIO.writelnString(headerLine2);
        GenericIO.writelnString(headerLine3);
        GenericIO.writelnString(headerLine4);

    }

    public synchronized void setBrokerState(BrokerState brokerState)
    {
        this.brokerState = brokerState;
        logState();
    }

    public synchronized void setSpectatorState(SpectatorState spectatorState, int spectatorId)
    {
        if (this.spectatorStates[spectatorId] != spectatorState)
        {
            this.spectatorStates[spectatorId] = spectatorState;
            logState();
        }
    }

    public synchronized void setMoneyAmount(int spectatorMoneyAmount, int spectatorId)
    {
        if (this.moneyAmount[spectatorId] != spectatorMoneyAmount)
        {
            this.moneyAmount[spectatorId] = spectatorMoneyAmount;
            logState();
        }
    }

    public synchronized void setRaceNumber(int raceNumber)
    {
        this.raceNumber = raceNumber;
        setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
        //logState();
    }

    public synchronized void setHorseJockeyState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId)
    {
        if (this.horseJockeyState[raceId][horseJockeyId] != horseJockeyState)
        {
            this.horseJockeyState[raceId][horseJockeyId] = horseJockeyState;
            logState();
        }
    }

    public synchronized void setMaxMovingLength(int maxMovingLength, int horseId, int raceId)
    {
        if (this.maxMovingLength[raceId][horseId] != maxMovingLength)
        {
            this.maxMovingLength[raceId][horseId] = maxMovingLength;
            //logState();
        }
    }

    public synchronized void setDistanceInRace(int distanceInRace)
    {
        if (this.distanceInRace != distanceInRace)
        {
            this.distanceInRace = distanceInRace;
            //logState();
        }
    }

    public synchronized void setSpectatorBet(int spectatorBetAmount, int spectatorBetSelection, int spectatorMoneyAmount, int specId)
    {
        this.spectatorBetAmount[this.raceNumber][specId] = spectatorBetAmount;
        this.spectatorBetSelection[this.raceNumber][specId] = spectatorBetSelection;
        this.moneyAmount[specId] = spectatorMoneyAmount;
        logState();
    }

    public synchronized void setHorseOdds(double[] horseOdds, int raceId)
    {
        if (!Arrays.equals(this.horseOdds[raceId], horseOdds))
        {
            System.arraycopy(horseOdds, 0, this.horseOdds[raceId], 0, horseOdds.length);
            //logState();
        }
    }

    public synchronized void setHorseMove(int horseIteration, int horsePosition, int horseId, int raceId)
    {
        this.horseIteration[raceId][horseId] = horseIteration;
        this.horsePosition[raceId][horseId] = horsePosition;
        logState();
    }

    public synchronized void setHorseAtEnd(boolean horseAtEnd, int horseId, int raceId)
    {
        this.horseAtEnd[raceId][horseId] = horseAtEnd;
        //logState();
    }

    public synchronized void setHorseAtEnd(boolean horseAtEnd, int horseId, int raceId, HorseJockeyState horseJockeyState)
    {
        this.horseAtEnd[raceId][horseId] = horseAtEnd;
        this.horseJockeyState[raceId][horseId] = horseJockeyState;
        logState();
    }

    public synchronized void logState()
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

        line1 += "  " + this.raceNumber;

        for (int i = 0; i < SimulPar.N_numCompetitors; i++)
        {
            HorseJockeyState hj = horseJockeyState[this.raceNumber][i];

            int maxMovLen = maxMovingLength[this.raceNumber][i];

            line1 += " " + String.format("%-3s", hj != null ? hj.getAcronym() : "###");

            line1 += "  " + String.format("%-2" + (maxMovLen != -1 ? "d" : "s"),
                    maxMovLen != -1 ? maxMovLen : "##") + " ";
        }

        //String line2  = "  "+this.raceNumber+"  "+this.distanceInRace[this.raceNumber]+"  ";
        String line2 = "  " + this.raceNumber + "  " + this.distanceInRace + " ";

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

            line2 += "  " + (horseAtEnd[this.raceNumber][i] ? "T" : "F");
        }

        GenericIO.writelnString(line1);
        GenericIO.writelnString(line2);

    }

}
