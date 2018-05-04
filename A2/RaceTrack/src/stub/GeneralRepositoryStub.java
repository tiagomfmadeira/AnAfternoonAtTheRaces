package stub;

import communication.Message;
import communication.MessageType;
import entities.BrokerState;
import entities.HorseJockeyState;
import settings.Settings;
import static communication.Exchange.exchange;

public class GeneralRepositoryStub
{

    /**
     * Nome do sistema computacional onde está localizado o servidor.
     */
    private static String serverHostName;

    /**
     * Número do port de escuta do servidor.
     */
    private static int serverPortNumb;

    /**
     * Instanciação do stub.
     *
     * @param hostName nome do sistema computacional onde está localizado o
     *                 servidor
     * @param port     número do port de escuta do servidor
     */
    public GeneralRepositoryStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

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