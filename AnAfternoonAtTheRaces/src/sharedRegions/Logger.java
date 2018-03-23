package sharedRegions;

import entities.*;
import genclass.FileOp;
import genclass.GenericIO;
import genclass.TextFile;
import main.SimulPar;

import static main.SimulPar.N_numCompetitors;

public class Logger {

    private BrokerState brokerState;
    private SpectatorState[] spectatorStates = new SpectatorState[SimulPar.M_numSpectators];
    private int[] moneyAmount = new int[SimulPar.M_numSpectators];
    private int raceNumber;//broker
    private HorseJockeyState[] horseJockeyState = new HorseJockeyState[SimulPar.N_numCompetitors];
    private int[] maxMovingLength = new int[SimulPar.N_numCompetitors];
    //private int[] distanceInRace = new int[SimulPar.K_numRaces];
    private int distanceInRace;
    private int[] spectatorBetSelection = new int[SimulPar.M_numSpectators];
    private int[] spectatorBetAmount = new int[SimulPar.M_numSpectators];
    private double[] horseOdds = new double[SimulPar.N_numCompetitors];
    private int[] n_iteration = new int[SimulPar.N_numCompetitors];
    private boolean[] horseAtEnd = new boolean[SimulPar.N_numCompetitors];
    private int[] horsePosition = new int[SimulPar.N_numCompetitors];

    public Logger(){
        String headerLine1 = "MAN/BRK           SPECTATOR/BETTER              HORSE/JOCKEY PAIR at Race RN";

        String headerLine2 = "  Stat ";
        for(int i = 0; i < SimulPar.M_numSpectators;i++){
            headerLine2 += " St"+i+"  "+"Am"+i;
        }
        headerLine2+=" RN";
        for(int i = 0; i < SimulPar.N_numCompetitors;i++){
            headerLine2 += " St"+i+" "+"Len"+i;
        }

        String headerLine3 = String.format("%1$"+45+"s","Race RN Status");

        String headerLine4 = " RN Dist";
        for(int i = 0; i < SimulPar.M_numSpectators;i++){
            headerLine4 += " BS"+i+"  "+"BA"+i;
        }
        headerLine4+=" ";
        for(int i = 0; i < SimulPar.N_numCompetitors;i++){
            headerLine4 += " Od"+i+" "+"N"+i+" "+"Ps"+i+" SD"+i;
        }

        GenericIO.writelnString(headerLine1);
        GenericIO.writelnString(headerLine2);
        GenericIO.writelnString(headerLine3);
        GenericIO.writelnString(headerLine4);

    }

    public synchronized void setBrokerState(BrokerState brokerState) {
        this.brokerState = brokerState;
        logState();
    }

    public synchronized void setSpectatorState(SpectatorState spectatorState, int spectatorId) {
        this.spectatorStates[spectatorId] = spectatorState;
        logState();
    }

    public synchronized void setMoneyAmount(int spectatorMoneyAmount, int spectatorId) {
        this.moneyAmount[spectatorId] = spectatorMoneyAmount;
        logState();
    }

    public synchronized void setRaceNumber(int raceNumber) {
        this.raceNumber = raceNumber;
        logState();
    }

    public synchronized void setHorseJockeyState(HorseJockeyState horseJockeyState, int horseJockeyId) {
        this.horseJockeyState[horseJockeyId] = horseJockeyState;
        logState();
    }

    public synchronized void setMaxMovingLength(int maxMovingLength, int horseId) {
        this.maxMovingLength[horseId] = maxMovingLength;
        logState();
    }

    public synchronized void setDistanceInRace(int distanceInRace) {
        this.distanceInRace = distanceInRace;
        logState();
    }

    public synchronized void setSpectatorBetSelection(int spectatorBetSelection, int specId) {
        this.spectatorBetSelection[specId] = spectatorBetSelection;
        logState();
    }

    public synchronized void setSpectatorBetAmount(int spectatorBetAmount, int specId) {
        this.spectatorBetAmount[specId] = spectatorBetAmount;
        logState();
    }

    public synchronized void setHorseOdds(double[] horseOdds) {
        System.arraycopy(horseOdds, 0, this.horseOdds, 0,horseOdds.length);
        logState();
    }

    public synchronized void setN_iteration(int[] n_iteration) {
        this.n_iteration = n_iteration;
        logState();
    }

    public synchronized void setHorsePosition(int[] horsePosition) {
        this.horsePosition = horsePosition;
        logState();
    }

    public synchronized void setHorseAtEnd(boolean[] horseAtEnd) {
        this.horseAtEnd = horseAtEnd;
        logState();
    }


    public synchronized void logState(){
        // retrieve variables
        String line1 = "  "+ String.format("%4s",brokerState != null ? brokerState.getAcronym() : "####")+" ";

        for(int i = 0; i < SimulPar.M_numSpectators;i++) {
            SpectatorState s = spectatorStates[i];
            line1 += " "+ String.format( "%-3s",s != null ? s.getAcronym() : "###");
            line1 += " "+ String.format( "%-4d",moneyAmount[i]);
        }

        line1+="  "+this.raceNumber;

        for(int i = 0; i < SimulPar.N_numCompetitors;i++){
            HorseJockeyState hj = horseJockeyState[i];

            line1 += " "+String.format("%-3s",hj != null ? hj.getAcronym() : "###");
            line1 += "  "+String.format("%-2d",maxMovingLength[i])+" ";
        }

        //String line2  = "  "+this.raceNumber+"  "+this.distanceInRace[this.raceNumber]+"  ";
        String line2  = "  "+this.raceNumber+"  "+this.distanceInRace+"  ";


        for(int i=0;i < SimulPar.M_numSpectators; i++) {

            line2 += "  "+spectatorBetSelection[i];
            line2 += "  "+String.format("%-4d",spectatorBetAmount[i]);

        }

        for(int i=0;i < SimulPar.N_numCompetitors; i++) {
            line2+=" "+String.format("%-4.2f",horseOdds[i]);
            line2+=" "+String.format("%-2d",n_iteration[i]);
            line2+="  "+String.format("%-2d", horsePosition[i]);
            line2+="  "+(horseAtEnd[i] ? "T" : "F");
        }

        GenericIO.writelnString(line1);
        GenericIO.writelnString(line2);

    }

}

