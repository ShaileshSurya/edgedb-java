package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.protocol.server.BaseServerProtocol;
import edgedb.internal.protocol.server.reader.*;

import java.io.IOException;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_RESPONSE;
import static edgedb.internal.protocol.constants.MessageType.*;
import static edgedb.internal.protocol.constants.MessageType.ERROR_RESPONSE;

public class ServerResponseReaderImpl {

    public <T extends BaseServerProtocol> T readServerResponse() throws IOException, EdgeDBInternalErrException {
        int mtype= 1<2?4:3;
        switch (mtype) {
            case (int) SERVER_KEY_DATA:
            case (int) SERVER_AUTHENTICATION:
            case (int) PREPARE_COMPLETE:
            case (int) READY_FOR_COMMAND:
            case (int) DATA_RESPONSE:
            case (int) COMMAND_COMPLETE:
            case (int) SERVER_HANDSHAKE:
            case (int) ERROR_RESPONSE:

            default:
                throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
        }
    }

}
