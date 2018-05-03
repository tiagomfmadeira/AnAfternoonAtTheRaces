package stub;

import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import static communication.Exchange.exchange;

public class RaceTrackStub
{

    /**
     * Nome do sistema computacional onde está localizado o servidor.
     */
    private String serverHostName;

    /**
     * Número do port de escuta do servidor.
     */
    private int serverPortNumb;

    /**
     * Instanciação do stub.
     *
     * @param hostName nome do sistema computacional onde está localizado o
     *                 servidor
     * @param port     número do port de escuta do servidor
     */
    public RaceTrackStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public void startTheRace()
    {
        Broker broker = (Broker) Thread.currentThread();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                broker.getCurrentRace()
        );

        exchange(msg, serverHostName, serverPortNumb);

        // change state
        broker.setBrokerState(BrokerState.SUPERVISING_THE_RACE);

    }

    public boolean[] reportResults()
    {
        int raceId = ((Broker) Thread.currentThread()).getCurrentRace();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                raceId
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (boolean[]) result.getReturnValue();
    }
}
