package communication;

import java.io.Serializable;

/**
 * General description: Definition of the message class, whose object can be
 * used as a vehicle for message exchange in TCP socket.
 */
public class Message implements Serializable
{

    private MessageType type = null;
    private String func = null;
    private Object args[] = null;
    private Object returnValue = null;

    /**
     * Constructor
     *
     * @param type the type of the message
     */
    public Message(MessageType type)
    {
        this.type = type;
    }

    /**
     * Constructor
     *
     * @param type the type of the message
     * @param func the name of the function to be called remotely
     */
    public Message(MessageType type, String func)
    {
        this(type);
        this.func = func;
        this.args = args;
    }

    /**
     * Constructor
     *
     * @param type the type of the message
     * @param func the name of the function to be called remotely
     * @param args arguments required by the function to be called
     */
    public Message(MessageType type, String func, Object... args)
    {
        this(type);
        this.func = func;
        this.args = args;
    }

    /**
     * Constructor
     *
     * @param returnValue the object to be sent to the client in a response
     *                    message.
     */
    public Message(Object returnValue)
    {
        this(MessageType.ACK);
        this.returnValue = returnValue;
    }

    /**
     * Returns the type of the message
     *
     * @return the type of the message
     */
    public MessageType getType()
    {
        return type;
    }

    /**
     * Returns the function name within the message
     *
     * @return the function name within the message
     */
    public String getFunc()
    {
        return func;
    }

    /**
     * Returns the arguments within the message, required by the function to be
     * called.
     *
     * @return the arguments within the message
     */
    public Object[] getArgs()
    {
        return args;
    }

    /**
     * Returns the object to be sent back to the client in a response message.
     *
     * @return the object to be sent back to the client
     */
    public Object getReturnValue()
    {
        return returnValue;
    }
}
