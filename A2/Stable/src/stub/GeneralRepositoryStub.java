package stub;

import communication.Message;
import communication.MessageType;
import entities.HorseJockeyState;
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
     * execute the <code>setRaceNumber<code> function in the remote location.
     * Sends the message using the exchange method.
     *
     * @param raceNumber the argument required for the function. To be inserted
     *                   into the message.
     */
    public void setRaceNumber(int raceNumber)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                raceNumber
        );

        exchange(msg, serverHostName, serverPortNumb);

    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>setHorseJockeyState<code> function in the remote location.
     * Sends the message using the exchange method.
     *
     * @param horseJockeyState one of the arguments required for the function.
     *                         To be inserted into the message.
     * @param horseJockeyId    one of the arguments required for the function.
     *                         To be inserted into the message.
     * @param raceId           one of the arguments required for the function.
     *                         To be inserted into the message.
     */
    public void setHorseJockeyState(HorseJockeyState horseJockeyState, int horseJockeyId, int raceId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                horseJockeyState,
                horseJockeyId,
                raceId
        );

        exchange(msg, serverHostName, serverPortNumb);
    }

    /**
     * Creates a message to request the current settings from the General
     * Repository, in order to obtain the address where the server should be
     * created at. Sends it using the exchange method. Processes the reply.
     *
     * @return the settings instance received
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
