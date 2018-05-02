package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;

public class BettingCenterStub {

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

    public BettingCenterStub(String hostName, int port) {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
        this.com = new ClientCom(serverHostName, serverPortNumb);
    }

    public void acceptTheBets(double[] horseJockeyOdds)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                    MessageType.FUNCTION,
                    new Object(){}.getClass().getEnclosingMethod().getName(),
                    (Object)horseJockeyOdds
        );

        Message result = com.exchange(msg);

    }

    public boolean areThereAnyWinners(boolean[] horseJockeyWinners)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                (Object)horseJockeyWinners
        );

        Message result = com.exchange(msg);

        return (boolean)result.getReturnValue();
    }

    public void honourTheBets()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);

    }

    public void placeABet(int horseJockeyID)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                (Object)horseJockeyID
        );

        Message result = com.exchange(msg);

    }


    public void goCollectTheGains()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);
    }


}
