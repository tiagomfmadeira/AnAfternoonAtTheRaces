package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;
import entities.HorseJockey;
import entities.HorseJockeyState;
import entities.Spectator;
import entities.SpectatorState;

import static stub.Exchange.exchange;

public class PaddockStub {
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

    public PaddockStub(String hostName, int port) {
        this.serverHostName = hostName;
        this.serverPortNumb = port;
    }

    public boolean proceedToPaddock()
    {
        HorseJockey hj = ((HorseJockey) Thread.currentThread());

        // change HorseJockey state to AT_THE_PADDOCK
        hj.setHorseJockeyState(HorseJockeyState.AT_THE_PADDOCK);

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

    public void sleepAtThePaddock()
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        exchange(msg, serverHostName, serverPortNumb);

    }

    public void proceedToStartLine()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        exchange(msg, serverHostName, serverPortNumb);

    }

    public boolean goCheckHorses()
    {

        //  Change Spectator state to APPRAISING_THE_HORSES

        Spectator spec = ((Spectator) Thread.currentThread());
        spec.setSpectatorState(SpectatorState.APPRAISING_THE_HORSES);

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                spec.getSpectatorID()

        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (boolean) result.getReturnValue();
    }


    public int appraisingHorses()
    {
        int specId = ((Spectator) Thread.currentThread()).getSpectatorID();

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName(),
                specId
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);

        return (int) result.getReturnValue();
    }

    public double[] learnTheOdds()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);


        return (double[]) result.getReturnValue();
    }


}
