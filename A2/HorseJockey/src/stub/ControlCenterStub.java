package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;
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

    private ClientCom com;

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
     * execute the <code>proceedToPaddock<code> function in the remote location.
     * Sends the message using the exchange method.
     */
    public void proceedToPaddock()
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
     * execute the <code>makeAMove<code> function in the remote location.
     * Sends the message using the exchange method.
     */
    public void makeAMove()
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
}
