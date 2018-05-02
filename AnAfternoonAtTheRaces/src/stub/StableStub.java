package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;

public class StableStub {
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

    public StableStub(String hostName, int port) {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
        this.com = new ClientCom(serverHostName, serverPortNumb);
    }


    public void summonHorsesToPaddock(int raceID)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                raceID
        );

        com.exchange(msg);

        // change Broker state to ANNOUNCING_NEXT_RACE
        ((Broker) Thread.currentThread()).setBrokerState(BrokerState.ANNOUNCING_NEXT_RACE);

    }

    public void proceedToStable()
    {

        HorseJockey hj = ((HorseJockey) Thread.currentThread());

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                hj.getRaceId()
        );

        com.exchange(msg);

    }

    public void proceedToStableFinal()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                hj.getHorseJockeyID(),
                hj.getRaceId()
        );

        com.exchange(msg);
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_STABLE);


    }

}
