package stub;

import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import static communication.Exchange.exchange;

public class BettingCenterStub
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
    public BettingCenterStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public void acceptTheBets(double[] horseJockeyOdds)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                (Object) horseJockeyOdds
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);

    }

    public boolean areThereAnyWinners(boolean[] horseJockeyWinners)
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                (Object) horseJockeyWinners
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (boolean) result.getReturnValue();
    }

    public void honourTheBets()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName()
        );

        exchange(msg, serverHostName, serverPortNumb);

        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.SETTLING_ACCOUNTS);
    }
}
