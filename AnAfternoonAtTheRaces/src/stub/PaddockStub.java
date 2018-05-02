package stub;

import communication.ClientCom;
import communication.Message;
import communication.MessageType;
import entities.HorseJockey;
import entities.HorseJockeyState;
import entities.Spectator;
import entities.SpectatorState;

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
        this.com = new ClientCom(serverHostName, serverPortNumb);
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

        Message result = com.exchange(msg);

        return (boolean) result.getReturnValue();
    }

    public void sleepAtThePaddock()
    {

        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        com.exchange(msg);

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

        Message result = com.exchange(msg);

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

        Message result = com.exchange(msg);

        return (int) result.getReturnValue();
    }

    public double[] learnTheOdds()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object(){}.getClass().getEnclosingMethod().getName()
        );

        Message result = com.exchange(msg);

        return (double[]) result.getReturnValue();
    }

}
