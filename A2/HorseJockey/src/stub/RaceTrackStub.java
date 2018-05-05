package stub;

import communication.Message;
import communication.MessageType;
import entities.HorseJockey;
import entities.HorseJockeyState;
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
     * execute the <code>proceedToStartLine<code> function in the remote location.
     * Sends the message using the exchange method. Updates the Horse/Jockey
     * local thread state.
     */
    public void proceedToStartLine()
    {
        HorseJockey hj = (HorseJockey) Thread.currentThread();

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

        // change state
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE);

    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>makeAMove<code> function in the remote location.
     * Sends the message using the exchange method. Processes the reply.
     *
     * @return the boolean produced by the remote function
     */
    public boolean makeAMove()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());

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
     * execute the <code>hasRaceEnded<code> function in the remote location.
     * Sends the message using the exchange method. Processes the reply.
     *
     * @return the boolean produced by the remote function
     */
    public boolean hasRaceEnded()
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

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (boolean) result.getReturnValue();
    }
}
