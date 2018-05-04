package stub;

import communication.Message;
import communication.MessageType;
import entities.BrokerState;
import entities.SpectatorState;
import settings.Settings;
import static communication.Exchange.exchange;

/**
 * General description: Definition of the General Repository stub.
 */
public class GeneralRepositoryStub
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
    public GeneralRepositoryStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>setBrokerState<code> function in the remote location.
     * Sends the message.
     *
     * @param brokerState the argument required for the function to be inserted
     *                    into the message
     */
    public void setBrokerState(BrokerState brokerState)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                brokerState
        );

        exchange(msg, serverHostName, serverPortNumb);

    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>setMoneyAmount<code> function in the remote location.
     * Sends the message.
     *
     * @param spectatorMoneyAmount one of the arguments required for the
     *                             function. To be inserted into the message.
     * @param spectatorId          one of the arguments required for the
     *                             function. To be inserted into the message.
     *
     */
    public void setMoneyAmount(int spectatorMoneyAmount, int spectatorId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                spectatorMoneyAmount,
                spectatorId
        );

        exchange(msg, serverHostName, serverPortNumb);

    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>setSpectatorState<code> function in the remote location.
     * Sends the message.
     *
     * @param spectatorState one of the arguments required for the function. To
     *                       be inserted into the message.
     * @param spectatorId    one of the arguments required for the function. To
     *                       be inserted into the message.
     */
    public void setSpectatorState(SpectatorState spectatorState, int spectatorId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                spectatorState,
                spectatorId
        );

        exchange(msg, serverHostName, serverPortNumb);
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>setSpectatorBet<code> function in the remote location.
     * Sends the message.
     *
     * @param spectatorBetAmount    one of the arguments required for the
     *                              function. To be inserted into the message.
     * @param spectatorBetSelection one of the arguments required for the
     *                              function. To be inserted into the message.
     * @param spectatorMoneyAmount  one of the arguments required for the
     *                              function. To be inserted into the message.
     * @param specId                one of the arguments required for the
     *                              function. To be inserted into the message.
     */
    public void setSpectatorBet(int spectatorBetAmount, int spectatorBetSelection, int spectatorMoneyAmount, int specId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                spectatorBetAmount,
                spectatorBetSelection,
                spectatorMoneyAmount,
                specId
        );

        exchange(msg, serverHostName, serverPortNumb);
    }

    /**
     * Requests the current settings in the General Repository for what address
     * the server should be created at.
     *
     * @return the settings instance
     */
    public Settings getSettings()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.SETTINGS,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName()
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);
        return (Settings) result.getReturnValue();
    }

}
