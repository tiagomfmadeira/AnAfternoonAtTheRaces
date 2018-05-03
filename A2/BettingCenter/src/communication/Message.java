package communication;

import java.io.Serializable;

public class Message implements Serializable {

    private MessageType type = null;
    private String func = null;
    private Object args[] = null;
    private Object returnValue = null;

    public Message(MessageType type) {
        this.type = type;
    }

    /**
     * Construct to create Message with type and Integer
     * @param type
     * @param func
     * @param args
     */
    public Message(MessageType type, String func, Object... args) {
        this(type);
        this.func = func;
        this.args = args;
    }

    /**
     * Construct to create Message with type and Integer
     * @param type
     * @param func
     */
    public Message(MessageType type, String func) {
        this(type);
        this.func = func;
        this.args = args;
    }

    /**
     * Construct to create Message with type and Integer
     * @param returnValue
     */
    public Message(Object returnValue) {
        this(MessageType.ACK);
        this.returnValue = returnValue;
    }

    public MessageType getType() {
        return type;
    }

    public String getFunc() {
        return func;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
