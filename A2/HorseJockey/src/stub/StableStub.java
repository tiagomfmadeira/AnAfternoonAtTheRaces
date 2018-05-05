package stub;

import communication.Message;
import communication.MessageType;
import entities.HorseJockey;
import entities.HorseJockeyState;
import static communication.Exchange.exchange;

/**
 * General description: Definition of the Stable stub.
 */
public class StableStub
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
    public StableStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>proceedToStable</code> function in the remote location.
     * Sends the message using the exchange method.
     */
    public void proceedToStable()
    {

        HorseJockey hj = ((HorseJockey) Thread.currentThread());

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                hj.getRaceId()
        );

        exchange(msg, serverHostName, serverPortNumb);

    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>proceedToStableFinal</code> function in the remote
     * location. Sends the message using the exchange method. Updates the
     * Horse/Jockey local thread state.
     */
    public void proceedToStableFinal()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                hj.getHorseJockeyID(),
                hj.getRaceId()
        );

        exchange(msg, serverHostName, serverPortNumb);
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_STABLE);
    }
}
