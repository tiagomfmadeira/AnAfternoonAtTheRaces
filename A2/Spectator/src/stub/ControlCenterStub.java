package stub;

import communication.Message;
import communication.MessageType;
import entities.Spectator;
import entities.SpectatorState;
import static communication.Exchange.exchange;

/**
 * General description: Definition of the Control Center stub.
 */
public class ControlCenterStub
{

    /**
     * Name of the computer system where the server is located.
     */
    private static String serverHostName;

    /**
     * Number of the listener port of the server.
     */
    private static int serverPortNumb;

    /**
     * Constructor
     *
     * @param hostName the name of the computer system where the server is
     *                 located
     * @param port     the number of the listener port of the server
     */
    public ControlCenterStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>waitForNextRace</code> function in the remote location.
     * Sends the message using the exchange method. Processes the reply message.
     * Updates the Spectator local thread state.
     *
     * @return the boolean value produced by the remote function
     */
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

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>lastToCheckHorses</code> function in the remote
     * location. Sends the message using the exchange method.
     */
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

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>goWatchTheRace</code> function in the remote location.
     * Sends the message using the exchange method. Updates the Spectator local
     * thread state.
     */
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

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>haveIWon</code> function in the remote location. Sends
     * the message using the exchange method. Processes the reply message.
     *
     * @param horseJockey the ID of the Horse/Jockey pair
     *
     * @return the boolean value produced by the remote function
     */
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

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>waitForNextRace</code> function in the remote location.
     * Sends the message using the exchange method. Updates the Spectator local
     * thread state.
     */
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
