package edgedb.protocol.constants;

//TODO:- Should we break this and move to protocols? Like for sent by ClientProtocols and ServerProtocols.
public final class MessageType {
    private MessageType() {
    }

    public static final char CLIENT_HANDSHAKE = 'V';


    public static final char SERVER_HANDSHAKE = 'v';
    public static final char AUTHENTICATION_OK = 'R';
    public static final char ERROR_RESPONSE = 'E';
    public static final char COMMAND_COMPLETE = 'C';
    public static final char SERVER_KEY_DATA = 'K';
}
