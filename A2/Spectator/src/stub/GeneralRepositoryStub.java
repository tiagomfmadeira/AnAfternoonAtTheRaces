package stub;

import communication.Message;
import communication.MessageType;
import entities.SpectatorState;
import settings.Settings;
import static communication.Exchange.exchange;

public class GeneralRepositoryStub
{

    /**
     * Nome do sistema computacional onde está localizado o servidor.
     */
    private static String serverHostName;

    /**
     * Número do port de escuta do servidor.
     */
    private static int serverPortNumb;

    /**
     * Instanciação do stub.
     *
     * @param hostName nome do sistema computacional onde está localizado o
     *                 servidor
     * @param port     número do port de escuta do servidor
     */
    public GeneralRepositoryStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    public void setSpectatorInitialState(SpectatorState spectatorState, int spectatorId, int money)
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.FUNCTION,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName(),
                spectatorState,
                spectatorId,
                money
        );

        exchange(msg, serverHostName, serverPortNumb);
    }

    public Settings getSettings()
    {
        //conversão do metodo a invocar numa mensagem
        Message msg = new Message(
                MessageType.SETTINGS,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName()
        );

        Message result = exchange(msg, serverHostName, serverPortNumb);
        return (Settings) result.getReturnValue();
    }

    public void shutdown()
    {
        Message msg = new Message(
                MessageType.TERMINATE,
                new Object()
                {
                }.getClass().getEnclosingMethod().getName()
        );
        exchange(msg, serverHostName, serverPortNumb);
    }
}
