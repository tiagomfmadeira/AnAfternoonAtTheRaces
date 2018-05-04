package communication;

import java.io.*;
import java.net.*;

/**
 * General description: This datatype implements the client side communication
 * channel for a message based communication between systems through TCP socket
 * communication in server/client fashion. The data transfer is object based.
 */
public class ClientCom
{

    /**
     * Communication socket
     *
     * @serialField commSocket
     */
    private Socket commSocket = null;

    /**
     * Name of the computational system where the server is located.
     */
    private String serverHostName = null;

    /**
     * Listening port number of the server.
     */
    private int serverPortNumb;

    /**
     * Input stream of the communication channel.
     */
    private ObjectInputStream in = null;

    /**
     * Output stream of the communication channel.
     */
    private ObjectOutputStream out = null;

    /**
     * Instantiation of a communication channel.
     *
     * @param hostName the name of the computational system where the server is
     *                 located
     * @param portNumb the listening port number of the server
     */
    public ClientCom(String hostName, int portNumb)
    {
        serverHostName = hostName;
        serverPortNumb = portNumb;

        while (!this.open())
        {
            try
            {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e)
            {
            }
        }

    }

    /**
     * Opening of the communication channel. Instantiation of a socket of
     * communication and its association to the server's address. Opening the
     * input and output streams of the socket.
     *
     * @return <code>true</code> if the communication channel was opened
     *         successfully; <code>false</code> otherwise
     */
    public boolean open()
    {
        boolean success = true;
        SocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPortNumb);

        try
        {
            commSocket = new Socket();
            commSocket.connect(serverAddress);
        } catch (UnknownHostException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - o nome do sistema computacional onde reside o servidor é desconhecido: "
                    + serverHostName + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NoRouteToHostException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - o nome do sistema computacional onde reside o servidor é inatingível: "
                    + serverHostName + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (ConnectException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - o servidor não responde em: " + serverHostName + "." + serverPortNumb + "!");
            if (e.getMessage().equals("Connection refused"))
            {
                success = false;
            } else
            {
                System.out.println(e.getMessage() + "!");
                e.printStackTrace();
                System.exit(1);
            }
        } catch (SocketTimeoutException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - ocorreu um time out no estabelecimento da ligação a: "
                    + serverHostName + "." + serverPortNumb + "!");
            success = false;
        } catch (IOException e)                           // erro fatal --- outras causas
        {
            System.out.println(Thread.currentThread().getName()
                    + " - ocorreu um erro indeterminado no estabelecimento da ligação a: "
                    + serverHostName + "." + serverPortNumb + "!");
            e.printStackTrace();
            System.exit(1);
        }

        if (!success)
        {
            return (success);
        }

        try
        {
            out = new ObjectOutputStream(commSocket.getOutputStream());
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir o canal de saída do socket!");
            e.printStackTrace();
            System.exit(1);
        }

        try
        {
            in = new ObjectInputStream(commSocket.getInputStream());
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir o canal de entrada do socket!");
            e.printStackTrace();
            System.exit(1);
        }

        return (success);
    }

    /**
     * Closing of the communication channel. Closing of the input and output
     * streams of the socket. Closing of the socket.
     */
    public void close()
    {
        try
        {
            in.close();
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o canal de entrada do socket!");
            e.printStackTrace();
            System.exit(1);
        }

        try
        {
            out.close();
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o canal de saída do socket!");
            e.printStackTrace();
            System.exit(1);
        }

        try
        {
            commSocket.close();
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o socket de comunicação!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Reading an object from the communication channel.
     *
     * @return the read object
     */
    public Object readObject()
    {
        Object fromServer = null;                            // objecto

        try
        {
            fromServer = in.readObject();
        } catch (InvalidClassException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - o objecto lido não é passível de desserialização!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - erro na leitura de um objecto do canal de entrada do socket de comunicação!");
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - o objecto lido corresponde a um tipo de dados desconhecido!");
            e.printStackTrace();
            System.exit(1);
        }

        return fromServer;
    }

    /**
     * Writing an object to the communication channel.
     *
     * @param toServer the object to be written
     */
    public void writeObject(Object toServer)
    {
        try
        {
            out.writeObject(toServer);
        } catch (InvalidClassException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - o objecto a ser escrito não é passível de serialização!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotSerializableException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - o objecto a ser escrito pertence a um tipo de dados não serializável!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - erro na escrita de um objecto do canal de saída do socket de comunicação!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
