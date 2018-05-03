package stub;

import communication.ClientCom;
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

    private ClientCom com;

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

    public double[] learnTheOdds()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName()
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (double[]) result.getReturnValue();
    }

}
