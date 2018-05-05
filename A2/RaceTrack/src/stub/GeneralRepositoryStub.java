package stub;

import communication.Message;
import communication.MessageType;
import entities.BrokerState;
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
     * execute the <code>setBrokerState<code> function in the remote location.
     * Sends the message using the exchange method.
     *
     * @param brokerState the argument required for the function. To be inserted
     *                    into the message.
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
     * execute the <code>setDistanceInRace<code> function in the remote location.
     * Sends the message using the exchange method.
     *
     * @param distanceInRace the argument required for the function. To be
     *                       inserted into the message.
     */
    public void setDistanceInRace(int[] distanceInRace)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                (Object) distanceInRace
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
     * Creates a message containing the name and the required arguments to
     * execute the <code>setHorseJockeyMove<code> function in the remote location.
     * Sends the message using the exchange method.
     *
     * @param horseIteration one of the arguments required for the function. To
     *                       be inserted into the message.
     * @param horsePosition  one of the arguments required for the function. To
     *                       be inserted into the message.
     * @param horseId        one of the arguments required for the function. To
     *                       be inserted into the message.
     * @param raceId         one of the arguments required for the function. To
     *                       be inserted into the message.
     */
    public void setHorseJockeyMove(int horseIteration, int horsePosition, int horseId, int raceId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                horseIteration,
                horsePosition,
                horseId,
                raceId
        );

        exchange(msg, serverHostName, serverPortNumb);
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>setHorseJockeyStands<code> function in the remote location.
     * Sends the message using the exchange method.
     *
     * @param horsesAtEnd one of the arguments required for the function. To be
     *                    inserted into the message.
     * @param raceId      one of the arguments required for the function. To be
     *                    inserted into the message.
     */
    public void setHorseJockeyStands(int[] horsesAtEnd, int raceId)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                horsesAtEnd,
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
