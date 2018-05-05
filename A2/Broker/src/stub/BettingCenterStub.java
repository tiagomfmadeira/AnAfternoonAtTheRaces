package stub;

import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import static communication.Exchange.exchange;

/**
 * General description: Definition of the Betting Center stub.
 */
public class BettingCenterStub
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
    public BettingCenterStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>acceptTheBets</code> function in the remote location.
     * Sends the message using the exchange method. Updates the Broker local
     * thread state.
     *
     * @param horseJockeyOdds the argument required for the function. To be
     *                        inserted into the message.
     */
    public void acceptTheBets(double[] horseJockeyOdds)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                (Object) horseJockeyOdds
        );

        exchange(msg, serverHostName, serverPortNumb);

        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);

    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>areThereAnyWinners</code> function in the remote
     * location. Sends the message using the exchange method. Processes the
     * reply message.
     *
     * @param horseJockeyWinners the argument required for the function. To be
     *                           inserted into the message.
     *
     * @return the boolean value produced by the remote function
     */
    public boolean areThereAnyWinners(boolean[] horseJockeyWinners)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                (Object) horseJockeyWinners
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (boolean) result.getReturnValue();
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>honourTheBets</code> function in the remote location.
     * Sends the message using the exchange method. Updates the Broker local
     * thread state.
     */
    public void honourTheBets()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName()
        );

        exchange(msg, serverHostName, serverPortNumb);

        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.SETTLING_ACCOUNTS);
    }
}
