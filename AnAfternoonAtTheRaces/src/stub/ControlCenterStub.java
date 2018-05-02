package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;

public class ControlCenterStub {
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

    public ControlCenterStub(String hostName, int port) {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
        this.com = new ClientCom(serverHostName, serverPortNumb);
    }

    public void proceedToPaddock()
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);
    }

    public void makeAMove()
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);
    }

    public boolean waitForNextRace()
    {


        Spectator spec = ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                specId
        );

        Message result = com.exchange(msg);

        // change state
        spec.setSpectatorState(SpectatorState.WAITING_FOR_A_RACE_TO_START);

        return (boolean) result.getReturnValue();
    }


    public void lastToCheckHorses()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);

    }


    public void goWatchTheRace()
    {
        //conversão do metodo a invocar numa mensagem
        Spectator spec = ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();

        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                specId

        );

        com.exchange(msg);

        // change state
        spec.setSpectatorState(SpectatorState.WATCHING_A_RACE);

    }

    public boolean haveIWon(int horseJockey)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                horseJockey
        );

        Message result = com.exchange(msg);

        return (boolean) result.getReturnValue();
    }

    public void relaxABit()
    {
        //  Change Spectator state to CELEBRATING
        //conversão do metodo a invocar numa mensagem
        Spectator spec = ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                specId
        );

        com.exchange(msg);

        //change state
        spec.setSpectatorState(SpectatorState.CELEBRATING);

    }

    public void summonHorsesToPaddock()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);
    }

    public void startTheRace()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);
    }

    public void reportResults(boolean[] horseJockeysDeclaredWinners)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                (Object)horseJockeysDeclaredWinners
        );

        com.exchange(msg);
    }

    public void entertainTheGuests()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);

        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);

    }





}
