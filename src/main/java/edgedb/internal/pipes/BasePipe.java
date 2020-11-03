package edgedb.internal.pipes;

import edgedb.client.SocketStream;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.protocol.client.writer.BaseWriter;
import edgedb.internal.protocol.server.BaseServerProtocol;
import edgedb.internal.protocol.server.reader.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_RESPONSE;
import static edgedb.internal.protocol.constants.MessageType.*;

@Data
@Slf4j
public abstract class BasePipe {
    protected SocketStream socketStream;

    public BasePipe(SocketStream socketStream) {
        this.socketStream = socketStream;
    }

    public <S extends Read, T extends BaseServerProtocol> T read(S reader) throws IOException, EdgeDBInternalErrException {
        return (T) reader.read();
    }

    public <S extends BaseWriter> void write(S writer) throws IOException {
        writer.write();
    }

    public <S extends BaseWriter> void writeAndFlush(S writer) throws IOException {
        writer.writeAndFlush();
    }

    public <T extends BaseServerProtocol> T readServerResponse() throws IOException, EdgeDBInternalErrException {

        byte mType = socketStream.getDataInputStream().readByte();
        log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);

        switch (mType) {
            case (int) SERVER_KEY_DATA:
                return read(new ServerKeyDataReader(socketStream.getDataInputStream()));
            case (int) SERVER_AUTHENTICATION:
                return read(new ServerAuthenticationReader(socketStream.getDataInputStream()));
            case (int) PREPARE_COMPLETE:
                return read(new PrepareCompleteReader(socketStream.getDataInputStream()));
            case (int) READY_FOR_COMMAND:
                return read(new ReadyForCommandReader(socketStream.getDataInputStream()));
            case (int) DATA_RESPONSE:
                return read(new DataResponseReader(socketStream.getDataInputStream()));
            case (int) COMMAND_COMPLETE:
                return read(new CommandCompleteReader(socketStream.getDataInputStream()));
            case (int) SERVER_HANDSHAKE:
                return read(new ServerHandshakeReader(socketStream.getDataInputStream()));
            case (int) ERROR_RESPONSE:
                return read(new ErrorResponseReader(socketStream.getDataInputStream()));


            default:
                throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
        }
    }
}
