package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;
import entities.HorseJockey;
import entities.HorseJockeyState;
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

    private ClientCom com;

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
     * execute the <code>proceedToPaddock<code> function in the remote location.
     * Sends the message using the exchange method. Processes the reply.
     *
     * @return the boolean produced by the remote function
     */
    public boolean proceedToPaddock()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());

        // change HorseJockey state to AT_THE_PADDOCK
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK);

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                hj.getHorseJockeyID(),
                hj.getRaceId(),
                hj.getAgility()
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (boolean) result.getReturnValue();
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>sleepAtThePaddock<code> function in the remote location.
     * Sends the message using the exchange method.
     */
    public void sleepAtThePaddock()
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
     * execute the <code>proceedToStartLine<code> function in the remote location.
     * Sends the message using the exchange method.
     */
    public void proceedToStartLine()
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
