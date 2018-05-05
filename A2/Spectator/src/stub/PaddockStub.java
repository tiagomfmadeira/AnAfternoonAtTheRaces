package stub;

import communication.Message;
import communication.MessageType;
import entities.Spectator;
import entities.SpectatorState;
import static communication.Exchange.exchange;

/**
 * General description: Definition of the Paddock stub.
 */
public class PaddockStub
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
    public PaddockStub(String hostName, int port)
    {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>goCheckHorses<code> function in the remote location.
     * Sends the message using the exchange method. Processes the reply message.
     *
     * @return the boolean value produced by the remote function
     */
    public boolean goCheckHorses()
    {

        //  Change Spectator state to APPRAISING_THE_HORSES
        Spectator spec = ((Spectator) Thread.currentThread());
        spec.setSpectatorState(SpectatorState.APPRAISING_THE_HORSES);

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                spec.getSpectatorID()
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (boolean) result.getReturnValue();
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>appraisingHorses<code> function in the remote location.
     * Sends the message using the exchange method. Processes the reply message.
     *
     * @return the integer value produced by the remote function
     */
    public int appraisingHorses()
    {
        int specId = ((Spectator) Thread.currentThread()).getSpectatorID();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                specId
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (int) result.getReturnValue();
    }
}
