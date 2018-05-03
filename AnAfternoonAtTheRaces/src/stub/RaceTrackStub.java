package stub;

import communication.Message;
import communication.MessageType;
import entities.Broker;
import entities.BrokerState;
import entities.HorseJockey;
import entities.HorseJockeyState;

import static communication.Exchange.exchange;

public class RaceTrackStub {
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
     * @param hostName nome do sistema computacional onde está localizado o servidor
     * @param port     número do port de escuta do servidor
     */

    public RaceTrackStub(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public void proceedToStartLine()
    {
        //  Change HorseJockey state to AT_THE_START_LINE
        HorseJockey hj = (HorseJockey) Thread.currentThread();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                hj.getHorseJockeyID(),
                hj.getRaceId()
        );

        exchange(msg, serverHostName, serverPortNumb);

        // change state
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_START_LINE);


    }

    public boolean makeAMove()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                hj.getHorseJockeyID(),
                hj.getRaceId(),
                hj.getAgility()
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);


        return (boolean) result.getReturnValue();

    }

    public boolean hasRaceEnded()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                hj.getRaceId()
        );

        Message result =  exchange(msg, serverHostName, serverPortNumb);


        return (boolean) result.getReturnValue();

    }

    public void startTheRace()
    {
        Broker broker = (Broker) Thread.currentThread();


        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
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
                new Object(){}.getClass().getEnclosingMethod().getName(),
                raceId
        );


        Message result =  exchange(msg, serverHostName, serverPortNumb);


        return (boolean[]) result.getReturnValue();
    }

    public void shutdown(){
        Message msg = new Message(
                MessageType.TERMINATE,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );
        exchange(msg, serverHostName, serverPortNumb);
    }

}
