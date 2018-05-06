package stub;

import communication.Message;
import communication.MessageType;
import static communication.Exchange.exchange;

/**
 * General description: Definition of the Paddock stub.
 */
public class PaddockStub
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
    public PaddockStub(String hostName, int port)
    {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
    }

    /**
     * Creates a message containing the name to execute the
     * <code>shutdown</code> function in the remote location. Sends the message
     * using the exchange method.
     */
    public void shutdown()
    {
        Message msg = new Message(
                MessageType.TERMINATE,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName()
        );
        exchange(msg, serverHostName, serverPortNumb);
    }

}
