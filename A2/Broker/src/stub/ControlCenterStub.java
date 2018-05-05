package stub;

import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import static communication.Exchange.exchange;

/**
 * General description: Definition of the Control Center stub.
 */
public class ControlCenterStub
{

    /**
     * Name of the computer system where the server is located.
     */
    private String serverHostName;

    /**
     * Number of the listener port of the server.
     */
    private int serverPortNumb;

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
     * execute the <code>summonHorsesToPaddock</code> function in the remote
     * location. Sends the message using the exchange method.
     */
    public void summonHorsesToPaddock()
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
     * execute the <code>startTheRace</code> function in the remote location.
     * Sends the message using the exchange method.
     */
    public void startTheRace()
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
     * execute the <code>reportResults</code> function in the remote location.
     * Sends the message using the exchange method.
     *
     * @param horseJockeysDeclaredWinners the argument required for the
     *                                    function. To be inserted into the
     *                                    message.
     */
    public void reportResults(boolean[] horseJockeysDeclaredWinners)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                (Object) horseJockeysDeclaredWinners
        );

        exchange(msg, serverHostName, serverPortNumb);
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>entertainTheGuests</code> function in the remote
     * location. Sends the message using the exchange method. Updates the Broker
     * local thread state.
     */
    public void entertainTheGuests()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName()
        );

        exchange(msg, serverHostName, serverPortNumb);

        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.PLAYING_HOST_AT_THE_BAR);
    }
}
