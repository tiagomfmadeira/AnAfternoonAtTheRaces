package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import entities.Spectator;
import entities.SpectatorState;

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

        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.WAITING_FOR_BETS);

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

        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.SETTLING_ACCOUNTS);


    }

    public void placeABet(int horseJockeyID)
    {
        //  Change Spectator state to PLACING_A_BET
        Spectator spec = ((Spectator) Thread.currentThread());
        int specId = spec.getSpectatorID();

        spec.setSpectatorState(SpectatorState.PLACING_A_BET);

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                horseJockeyID,
                specId,
                spec.getWalletValue()
        );

        Message result = com.exchange(msg);

        spec.updateWalletValue((int)result.getReturnValue());
    }


    public void goCollectTheGains()
    {
        Spectator spec = ((Spectator) Thread.currentThread());
        spec.setSpectatorState(SpectatorState.COLLECTING_THE_GAINS);

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                spec.getSpectatorID()
        );

        Message result = com.exchange(msg);

        spec.updateWalletValue((int)result.getReturnValue());
    }


}
