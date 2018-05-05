package stub;

import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import static communication.Exchange.exchange;

/**
 * General description: Definition of the Race Track stub.
 */
public class RaceTrackStub
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
    public RaceTrackStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>startTheRace<code> function in the remote location.
     * Sends the message using the exchange method. Updates the Broker local thread state.
     */
    public void startTheRace()
    {
        Broker broker = (Broker) Thread.currentThread();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                broker.getCurrentRace()
        );

        exchange(msg, serverHostName, serverPortNumb);

        // change state
        broker.setBrokerState(BrokerState.SUPERVISING_THE_RACE);

    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>reportResults<code> function in the remote location.
     * Sends the message using the exchange method. Processes the reply message.
     *
     * @return the array of booleans produced by the remote function
     */
    public boolean[] reportResults()
    {
        int raceId = ((Broker) Thread.currentThread()).getCurrentRace();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                raceId
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (boolean[]) result.getReturnValue();
    }
}
