package stub;

import communication.Message;
import communication.MessageType;
import static communication.Exchange.exchange;

public class PaddockStub
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
    public PaddockStub(String hostName, int port)
    {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
    }

    public void shutdown()
    {
        Message msg = new Message(
                MessageType.TERMINATE,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );
        exchange(msg, serverHostName, serverPortNumb);
    }

}
