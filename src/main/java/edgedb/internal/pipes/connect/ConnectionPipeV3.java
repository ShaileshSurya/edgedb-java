package edgedb.internal.pipes.connect;

import edgedb.client.SingletonBuffer;
import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.pipes.SyncFlow.SyncPipe;
import edgedb.internal.pipes.SyncFlow.SyncPipeImpl;
import edgedb.internal.protocol.client.*;
import edgedb.internal.protocol.client.writerV2.*;
import edgedb.internal.protocol.server.*;
import edgedb.internal.protocol.server.readerv2.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static edgedb.exceptions.ErrorMessage.DRIVER_INCOMPATIBLE_ERROR;
import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_TRANSACTION_STATE;
import static edgedb.internal.protocol.constants.CommonConstants.BUFFER_SIZE;
import static edgedb.internal.protocol.constants.TransactionState.*;

@Data
@Slf4j
public class ConnectionPipeV3 implements IConnectionPipe {
    ProtocolWriter protocolWriter;
    BufferReader bufferReader;

    public ConnectionPipeV3(SocketChannel channel) {
        this.protocolWriter = new ChannelProtocolWriterImpl(channel);
        this.bufferReader = new BufferReaderImpl(channel);
    }

    @Override
    public void connect(String user, String database) throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException {
            ClientHandshake clientHandshakeMessage = new ClientHandshake(user,database);

            protocolWriter.write(clientHandshakeMessage);
            tryConnect();

            log.info("Connection Successful, Ready for command.");
    }

    public <T extends BaseServerProtocol> void waitForReadyToCommand() throws IOException, EdgeDBInternalErrException {
        log.info("Waiting for ready to command message");
        SyncPipe syncPipe = new SyncPipeImpl(protocolWriter);
        syncPipe.sendSyncMessage();

        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();

        readBuffer = bufferReader.read(readBuffer);

        while (readBuffer.remaining() != -1) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);

            T response = reader.read(readBuffer);

            if (response instanceof ReadyForCommand) {
                ReadyForCommand readyForCommand = (ReadyForCommand) response;
                log.debug("Response is an Instance Of ReadyForCommand {}", readyForCommand);

                switch (readyForCommand.getTransactionState()) {
                    case (int) IN_FAILED_TRANSACTION:
                        //TODO: Coding to concrete implementation here. Watch out.
                        waitForReadyToCommand();
                        break;
                    case (int) IN_TRANSACTION:
                        break;
                    case (int) NOT_IN_TRANSACTION:
                        return;
                    default:
                        throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_TRANSACTION_STATE);
                }
            }

        }


    }

    @Override
    public void terminate() {
        try {
            log.info("Trying to disconnect client connection.");
            Terminate terminate = new Terminate();
            protocolWriter.write(terminate);

        } catch (Exception e){
            log.debug("Failed to terminate to EdgeDB Client Connection {}", e);
        }

    }

    private <T extends BaseServerProtocol> void tryConnect() throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException {

        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();

        readBuffer = bufferReader.read(readBuffer);

        PrepareComplete prepareComplete= null;

        while (readBuffer.hasRemaining()) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);

            T response = reader.read(readBuffer);
            log.info("~~~Response Found Was~{}~",response.toString());
            if (response instanceof ServerHandshake) {
                ServerHandshake serverHandshake = (ServerHandshake) response;
                log.debug("Response is an Instance Of ServerHandshake {}", serverHandshake);
                throw new EdgeDBIncompatibleDriverException(DRIVER_INCOMPATIBLE_ERROR,
                        serverHandshake.getMajorVersion(),
                        serverHandshake.getMinorVersion());
            }

            if (response instanceof ReadyForCommand) {
                ReadyForCommand readyForCommand = (ReadyForCommand) response;
                log.debug("Response is an Instance Of ReadyForCommand {}", readyForCommand);

                switch (readyForCommand.getTransactionState()) {
                    case (int) IN_FAILED_TRANSACTION:
                        //TODO: Coding to concrete implementation here. Watch out.
                        SyncMessage syncMessage = new SyncMessage();
                        protocolWriter.write(syncMessage);
                        break;
                    case (int) IN_TRANSACTION:
                        break;
                    case (int) NOT_IN_TRANSACTION:
                        break;
                    default:
                        throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_TRANSACTION_STATE);
                }
            }
        }
    }
}
