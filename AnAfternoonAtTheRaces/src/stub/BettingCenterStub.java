package stub;

import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;

import static communication.Exchange.exchange;

public class BettingCenterStub {

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

        return (boolean)result.getReturnValue();
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

    public void placeABet(int horseJockeyID)
    {
        //  Change Spectator state to PLACING_A_BET
        Spectator spec = ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                horseJockeyID,
                specId,
                spec.getWalletValue()
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        spec.setSpectatorState(SpectatorState.PLACING_A_BET);

        spec.updateWalletValue((int) result.getReturnValue());
    }

    public void goCollectTheGains()
    {
        Spectator spec = ((Spectator) Thread.currentThread());

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                spec.getSpectatorID(),
                spec.getWalletValue()
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        spec.setSpectatorState(SpectatorState.COLLECTING_THE_GAINS);

        spec.updateWalletValue((int) result.getReturnValue());
    }

    public void shutdown(){
        Message msg = new Message(
                MessageType.TERMINATE,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );
        exchange(msg, serverHostName, serverPortNumb);
    }

}
