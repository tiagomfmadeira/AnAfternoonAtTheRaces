package stub;

import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import static communication.Exchange.exchange;

public class StableStub
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
    public StableStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public void summonHorsesToPaddock(int raceID)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                raceID
        );

        exchange(msg, serverHostName, serverPortNumb);

        // change Broker state to ANNOUNCING_NEXT_RACE
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);
    }
}
