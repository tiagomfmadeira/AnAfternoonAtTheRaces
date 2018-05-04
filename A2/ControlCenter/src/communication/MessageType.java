package communication;

/**
 * General description: enum representing the existing types of messages.
 */
public enum MessageType
{
    /**
     * Type of message sent from the client to the server requesting a service
     * to be provided, indicates the function to be executed and necessary
     * arguments.
     */
    FUNCTION,
    /**
     * Type of message returned by the server to the client in response, may
     * contain arguments corresponding to results of a called function.
     */
    ACK,
    /**
     * Type of message containing the connection information.
     */
    SETTINGS,
    /**
     * Type of message sent to request the termination of a server.
     */
    TERMINATE
}
