package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;
import entities.BrokerState;
import entities.HorseJockeyState;
import entities.SpectatorState;
import settings.Settings;

public class GeneralRepositoryStub {
    /**
     * Nome do sistema computacional onde está localizado o servidor.
     */

    private String serverHostName;

    /**
     * Número do port de escuta do servidor.
     */

    private int serverPortNumb;


    private ClientCom com;

    /**
     * Instanciação do stub.
     *
     * @param hostName nome do sistema computacional onde está localizado o servidor
     * @param port     número do port de escuta do servidor
     */

    public GeneralRepositoryStub(String hostName, int port) {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
        this.com = new ClientCom(serverHostName, serverPortNumb);
    }


    public void setBrokerState(BrokerState brokerState)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                brokerState
        );

        com.exchange(msg);

    }

    public void setRaceNumber(int raceNumber)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                raceNumber
        );

        com.exchange(msg);

    }

    public void setDistanceInRace(int[] distanceInRace)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                (Object)distanceInRace
        );

        com.exchange(msg);

    }



    public void setMoneyAmount(int spectatorMoneyAmount, int spectatorId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                spectatorMoneyAmount,
                spectatorId
        );

        com.exchange(msg);

    }
    public void setSpectatorState(SpectatorState spectatorState, int spectatorId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                spectatorState,
                spectatorId
        );

        com.exchange(msg);
    }


    public void setSpectatorInitialState(SpectatorState spectatorState, int spectatorId, int money)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                spectatorState,
                spectatorId,
                money
        );

        com.exchange(msg);
    }

    public void setSpectatorBet(int spectatorBetAmount, int spectatorBetSelection, int spectatorMoneyAmount, int specId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                spectatorBetAmount,
                spectatorBetSelection,
                spectatorMoneyAmount,
                specId
        );

        com.exchange(msg);
    }

    public void setHorseJockeyState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                horseJockeyState,
                horseJockeyId,
                raceId
        );

        com.exchange(msg);
    }

    public void setHorseJockeyInitialState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId, int agility)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                horseJockeyState,
                horseJockeyId,
                raceId,
                agility
        );

        com.exchange(msg);
    }

    public void setHorseJockeyMove(int horseIteration, int horsePosition, int horseId, int raceId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                horseIteration,
                horsePosition,
                horseId,
                raceId
        );

        com.exchange(msg);
    }

    public void setHorseJockeyStands(int[] horsesAtEnd, int raceId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                horsesAtEnd,
                raceId
        );

        com.exchange(msg);
    }

    public Settings getSettings()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.SETTINGS,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        Message result = com.exchange(msg);
        return (Settings) result.getReturnValue();
    }


}
