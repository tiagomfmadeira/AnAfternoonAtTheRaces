package communication;

public class Exchange
{

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

        // fecho da ligação
        com.close();

        return result;
    }
}
