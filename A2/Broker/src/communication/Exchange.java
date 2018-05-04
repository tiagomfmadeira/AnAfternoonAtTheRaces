package communication;

/**
 * General description: Protocol definition of message exchange between the
 * client and the server.
 */
public class Exchange
{

    /**
     * Writes an object to the channel from the client side and read an object
     * incoming from the server side, making a sanity check of it's type.
     *
     * @param msg            the message to be sent from the client to the
     *                       server
     * @param serverHostName the name of the computational system where the
     *                       server is located
     * @param serverPortNumb the listening port number of the server
     *
     * @return the object read incoming from the server
     */
    public static Message exchange(Message msg, String serverHostName, int serverPortNumb)
    {
        ClientCom com = new ClientCom(serverHostName, serverPortNumb);

        // send msg to server
        com.writeObject(msg);

        Message result = (Message) com.readObject();

        if (result.getType() != MessageType.ACK)
        {
            System.out.println("invalid type" + result.toString());
            System.exit(1);
        }

        // close the channel
        com.close();

        return result;
    }
}
