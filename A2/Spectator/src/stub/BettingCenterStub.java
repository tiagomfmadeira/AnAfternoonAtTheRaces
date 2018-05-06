package stub;

import communication.Message;
import communication.MessageType;
import entities.Spectator;
import entities.SpectatorState;
import static communication.Exchange.exchange;

/**
 * General description: Definition of the Betting Center stub.
 */
public class BettingCenterStub
{

    /**
     * Name of the computer system where the server is located.
     */
    private String serverHostName;

    /**
     * Number of the listener port of the server.
     */
    private int serverPortNumb;

    /**
     * Constructor
     *
     * @param hostName the name of the computer system where the server is
     *                 located
     * @param port     the number of the listener port of the server
     */
    public BettingCenterStub(String hostName, int port)
    {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>placeABet</code> function in the remote location. Sends
     * the message using the exchange method. Processes the reply message.
     * Updates the Spectator local thread state and wallet value.
     *
     * @param horseJockeyID the argument required for the function. To be
     *                      inserted into the message.
     */
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

    /**
     * Creates a message containing the name and the required arguments to
     * execute the <code>goCollectTheGains</code> function in the remote
     * location. Sends the message using the exchange method. Processes the
     * reply message. Updates the Spectator local thread state and wallet value.
     */
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
}
