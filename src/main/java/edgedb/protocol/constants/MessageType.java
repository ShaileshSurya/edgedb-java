package edgedb.protocol.constants;

//TODO:- Should we break this and move to protocols? Like for sent by ClientProtocols and ServerProtocols.
public final class MessageType {
    private MessageType() {
    }

    public static final char CLIENT_HANDSHAKE = 'V';
    public static final char SYNC_MESSAGE = 'S';

    public static final char SERVER_HANDSHAKE = 'v';
    public static final char SERVER_AUTHENTICATION = 'R';
    public static final char ERROR_RESPONSE = 'E';
    public static final char COMMAND_COMPLETE = 'C';
    public static final char SERVER_KEY_DATA = 'K';


    public static final char PREPARE = 'P';
    public static final char PREPARE_COMPLETE = '1';

    public static final char READY_FOR_COMMAND= 'Z';

    public static final char AUTHENTICATION_SASL_INTIAL_RESPONSE = 'p';
    public static final char AUTHENTICATION_SASL_RESPONSE = 'r';
    public static final char TERMINATE = 'X';

    public static final char DATA_RESPONSE = 'D';

}
