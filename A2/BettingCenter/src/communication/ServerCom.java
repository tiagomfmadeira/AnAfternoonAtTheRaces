package communication;

import java.io.*;
import java.net.*;

/**
 * General description: This datatype implements the server side communication
 * channel for a message based communication between systems through TCP socket
 * communication in server/client fashion. The data transfer is object based.
 */
public class ServerCom
{

    /**
     * Listener socket
     *
     * @serialField listeningSocket
     */
    private ServerSocket listeningSocket = null;

    /**
     * Communication socket
     *
     * @serialField commSocket
     */
    private Socket commSocket = null;

    /**
     * Number of the server's listening port
     *
     * @serialField serverPortNumb
     */
    private int serverPortNumb;

    /**
     * Input stream of the communication channel.
     *
     * @serialField in
     */
    private ObjectInputStream in = null;

    /**
     * Output stream of the communication channel.
     *
     * @serialField out
     */
    private ObjectOutputStream out = null;

    /**
     * Instantiation of a communication channel.
     *
     * @param portNumb the number of the server's listening port
     */
    public ServerCom(int portNumb)
    {
        serverPortNumb = portNumb;
    }

    /**
     * Instantiation of a communication channel.
     *
     * @param portNumb the number of the server's listening port
     * @param lSocket  the listener socket
     */
    public ServerCom(int portNumb, ServerSocket lSocket)
    {
        serverPortNumb = portNumb;
        listeningSocket = lSocket;
    }

    /**
     * Service establishment. Instantiation of a listener socket of
     * communication and its association to the local machine's address and the
     * public listener port. Opening the input and output streams of the socket.
     */
    public void start()
    {
        try
        {
            listeningSocket = new ServerSocket(serverPortNumb);
            // 1 second timeout
            listeningSocket.setSoTimeout(1000);
        } catch (BindException e)                         // erro fatal --- port já em uso
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível a associação do socket de escuta ao port: "
                    + serverPortNumb + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e)                           // erro fatal --- outras causas
        {
            System.out.println(Thread.currentThread().getName()
                    + " - ocorreu um erro indeterminado na associação do socket de escuta ao port: "
                    + serverPortNumb + "!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Ending the service. Closing the listener socket.
     */
    public void end()
    {
        try
        {
            listeningSocket.close();
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o socket de escuta!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Listening protocol. Creation of a communication channel for a pending
     * request. Instantiation of a communication socket and it's association to
     * the client's address. Opening of the input and output streams of the
     * socket.
     *
     * @return the communication channel
     */
    public ServerCom accept()
    {
        ServerCom scon;                                      // canal de comunicação

        scon = new ServerCom(serverPortNumb, listeningSocket);
        try
        {
            scon.commSocket = listeningSocket.accept();
        } catch (SocketTimeoutException e)
        {
            return null;
        } catch (SocketException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - foi fechado o socket de escuta durante o processo de escuta!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir um canal de comunicação para um pedido pendente!");
            e.printStackTrace();
            System.exit(1);
        }

        // avoid race condition
        try
        {
            listeningSocket.setSoTimeout(1000);
        } catch (SocketException e)
        {
        }

        try
        {
            scon.in = new ObjectInputStream(scon.commSocket.getInputStream());
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir o canal de entrada do socket!");
            e.printStackTrace();
            System.exit(1);
        }

        try
        {
            scon.out = new ObjectOutputStream(scon.commSocket.getOutputStream());
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir o canal de saída do socket!");
            e.printStackTrace();
            System.exit(1);
        }

        return scon;
    }

    /**
     * Closing of the communication channel. Closing of the input and output
     * streams of the socket. Closing of the socket.
     */
    public void close()
    {
        try
        {
            if (in != null)
            {
                in.close();
            }
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o canal de entrada do socket!");
            e.printStackTrace();
            System.exit(1);
        }

        try
        {
            if (out != null)
            {
                out.close();
            }
        } catch (IOException e)
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o canal de saída do socket!");
            e.printStackTrace();
            System.exit(1);
        }

        try
        {
            if (commSocket != null)
            {
                commSocket.close();
            }
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
        Object fromClient = null;                            // objecto

        try
        {
            fromClient = in.readObject();
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

        return fromClient;
    }

    /**
     * Writing an object to the communication channel.
     *
     * @param toClient the object to be written
     */
    public void writeObject(Object toClient)
    {
        try
        {
            out.writeObject(toClient);
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
