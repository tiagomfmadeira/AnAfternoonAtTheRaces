package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;
import entities.Spectator;
import entities.SpectatorState;
import static communication.Exchange.exchange;

public class ControlCenterStub
{

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
     * @param hostName nome do sistema computacional onde está localizado o
     *                 servidor
     * @param port     número do port de escuta do servidor
     */
    public ControlCenterStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public boolean waitForNextRace()
    {
        Spectator spec = ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                specId
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        // change state
        spec.setSpectatorState(SpectatorState.WAITING_FOR_A_RACE_TO_START);

        return (boolean) result.getReturnValue();
    }

    public void lastToCheckHorses()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName()
        );

        exchange(msg, serverHostName, serverPortNumb);
    }

    public void goWatchTheRace()
    {
        //conversão do metodo a invocar numa mensagem
        Spectator spec = ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();

        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                specId
        );

        exchange(msg, serverHostName, serverPortNumb);

        // change state
        spec.setSpectatorState(SpectatorState.WATCHING_A_RACE);

    }

    public boolean haveIWon(int horseJockey)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                horseJockey
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

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
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                specId
        );

        exchange(msg, serverHostName, serverPortNumb);

        //change state
        spec.setSpectatorState(SpectatorState.CELEBRATING);

    }
}
