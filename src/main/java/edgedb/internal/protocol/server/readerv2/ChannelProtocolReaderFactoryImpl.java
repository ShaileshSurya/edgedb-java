package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import edgedb.internal.protocol.server.readerhelper.ChannelReaderHelperImpl;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_RESPONSE;
import static edgedb.internal.protocol.constants.MessageType.*;
import static edgedb.internal.protocol.constants.MessageType.ERROR_RESPONSE;

@Slf4j
public class ChannelProtocolReaderFactoryImpl implements ProtocolReaderFactory{
    IReaderHelper readerHelper;

    public ChannelProtocolReaderFactoryImpl(ByteBuffer readBuffer){
        this.readerHelper = new ChannelReaderHelperImpl(readBuffer);
    }

    @Override
    public ProtocolReader getProtocolReader(char mType, ByteBuffer readBuffer) throws EdgeDBInternalErrException {
        log.info("MType was found to be {}",mType);
        switch (mType) {
            case (int) SERVER_KEY_DATA:
                return new ServerKeyDataReaderV2(readerHelper);
            case (int) SERVER_AUTHENTICATION:
                return new ServerAuthenticationReaderV2(readerHelper);
            case (int) PREPARE_COMPLETE:
                return new PrepareCompleteReaderV2(readerHelper);
            case (int) READY_FOR_COMMAND:
                return new ReadyForCommandReaderV2(readerHelper);
            case (int) DATA_RESPONSE:
                return new DataResponseReaderV2(readerHelper);
            case (int) COMMAND_COMPLETE:
                log.info("~~~~~~~COMMAND_COMPLETE");
                break;
            case (int) SERVER_HANDSHAKE:
                log.info("~~~~~~~SERVER_HANDSHAKE");
                break;
            case (int) ERROR_RESPONSE:
                log.info("~~~~~~~ERROR_RESPONSE");
                return new ErrorResponseReaderV2(readerHelper);
            default:
                throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
        }
        return null;
    }
}
