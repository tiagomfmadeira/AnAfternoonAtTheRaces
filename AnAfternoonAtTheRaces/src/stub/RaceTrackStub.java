package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;

public class RaceTrackStub {
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
     * @param hostName nome do sistema computacional onde está localizado o servidor
     * @param port     número do port de escuta do servidor
     */

    public RaceTrackStub(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
        ClientCom com = new ClientCom(serverHostName, serverPortNumb);
    }

    public void proceedToStartLine()
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);

    }

    public boolean makeAMove()
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        Message result = com.exchange(msg);

        return (boolean) result.getReturnValue();

    }

    public boolean hasRaceEnded()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        Message result = com.exchange(msg);

        return (boolean) result.getReturnValue();

    }

    public void startTheRace()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);

    }


    public boolean[] reportResults()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );


        Message result = com.exchange(msg);

        return (boolean[]) result.getReturnValue();
    }

}
