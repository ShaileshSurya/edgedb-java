package edgedb.connection;

import edgedb.client.*;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.*;
import edgedb.internal.buffer.SingletonBuffer;
import edgedb.internal.pipes.SyncFlow.SyncPipe;
import edgedb.internal.pipes.SyncFlow.SyncPipeImpl;
import edgedb.internal.pipes.connect.ConnectionPipeV2;
import edgedb.internal.pipes.connect.IConnectionPipeV2;
import edgedb.internal.pipes.granularflow.GranularFlowPipeV2;
import edgedb.internal.pipes.granularflow.IGranularFlowPipe;
import edgedb.internal.protocol.*;
import edgedb.internal.protocol.client.writerV2.ChannelProtocolWritableImpl;
import edgedb.internal.protocol.constants.Cardinality;
import edgedb.internal.protocol.constants.IOFormat;
import edgedb.internal.protocol.server.readerfactory.ChannelProtocolReaderFactoryImpl;
import edgedb.internal.protocol.server.readerv2.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static edgedb.exceptions.ErrorMessage.DRIVER_INCOMPATIBLE_ERROR;
import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_TRANSACTION_STATE;
import static edgedb.internal.protocol.constants.TransactionState.*;

@Slf4j
public class BlockingConnection implements IConnection {

    SocketChannel clientChannel;

    byte[] serverKey;

    @Override
    public ResultSet query(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.BINARY, Cardinality.MANY, query);
    }

    @Override
    public ResultSet queryOne(String command) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.BINARY, Cardinality.ONE, command);
    }

    @Override
    public ResultSet execute(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.BINARY, Cardinality.MANY, query);
    }

    @Override
    public ResultSet queryJSON(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.JSON, Cardinality.MANY, query);
    }


    protected  <T extends ServerProtocolBehaviour> PrepareComplete readPrepareComplete(IGranularFlowPipe granularFlowPipe, Prepare prepareMessage) throws IOException, EdgeDBInternalErrException, EdgeDBCommandException {
        log.debug("Reading prepare complete");
        BufferReader bufferReader = new BufferReaderImpl(clientChannel);
        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();

        readBuffer = bufferReader.read(readBuffer);

        while (readBuffer.remaining()!= -1) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);

            T response = reader.read(readBuffer);

            if (response instanceof PrepareComplete) {
                log.debug("Response is an Instance Of PrepareComplete {}", (PrepareComplete) response);
                return (PrepareComplete) response;
            }

            if (response instanceof ErrorResponse) {
                log.debug("Response is an Instance Of Error {}", (ErrorResponse) response);
                ErrorResponse err = (ErrorResponse) response;
                throw new EdgeDBCommandException(err);
            }

            if (response instanceof ServerKeyDataBehaviour) {
                log.debug("Response is an Instance Of Error {}", (ServerKeyDataBehaviour) response);
                ServerKeyDataBehaviour serverKeyData = (ServerKeyDataBehaviour) response;
                this.serverKey = serverKeyData.getData();
                continue;
            }

            if (response instanceof ReadyForCommand) {
                log.debug("Response is an Instance Of ReadyForCommand {}", (ReadyForCommand) response);
                ReadyForCommand readyForCommand = (ReadyForCommand) response;

                switch (readyForCommand.getTransactionState()) {
                    case (int) IN_FAILED_TRANSACTION:
                        log.info("In Failed Transaction");
                        //TODO: Coding to concrete implementation here. Watch out.
                        SyncPipe syncPipe = new SyncPipeImpl(
                                new ChannelProtocolWritableImpl(getChannel()));
                        syncPipe.sendSyncMessage();
                        continue;
                    case (int) IN_TRANSACTION:
                        log.info("In Transaction");
                        throw new UnsupportedOperationException();
                    case (int) NOT_IN_TRANSACTION:
                        log.info("Not In Transaction");
                        break;
                    default:
                        throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_TRANSACTION_STATE);
                }

            }
        }

        if (readBuffer.remaining() == -1){
            // TODO: fix this
            throw new EdgeDBInternalErrException("Nothing to read in buffer");
        }
        return null;
    }

    protected ResultSet executeGranularFlow(char IOFormat, char cardinality, String command) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        IGranularFlowPipe granularFlowPipe = new GranularFlowPipeV2(
                new ChannelProtocolWritableImpl(getChannel()));

        Prepare prepareMessage = new Prepare(IOFormat, cardinality, command);
        granularFlowPipe.sendPrepareMessage(prepareMessage);
        PrepareComplete prepareComplete = readPrepareComplete(granularFlowPipe,prepareMessage);
        log.info("PrepareComplete received {}",prepareComplete);
//        try {
//            TypeDescriptor typeDescriptor = new TypeDecoderFactoryImpl().getTypeDescriptor(prepareComplete.getResultDataDescriptorID());
//        }catch (ScalarTypeNotFoundException e){
//
//        }
        granularFlowPipe.sendExecuteMessage(new Execute());
        return readDataResponse();
    }

    protected  <T extends ServerProtocolBehaviour> ResultSet readDataResponse() throws IOException, EdgeDBInternalErrException {
        log.debug("Reading DataResponse");
        DataResponse dataResponse = null;
        BufferReader bufferReader = new BufferReaderImpl(getChannel());
        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();;
        readBuffer = bufferReader.read(readBuffer);

        ResultSet resultSet = new ResultSetImpl();
        while (true) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);
            T response = reader.read(readBuffer);

            if (response instanceof DataResponse) {
                log.debug("Response is an Instance Of DataResponse {}", (DataResponse) response);
                dataResponse = (DataResponse) response;
                resultSet.setResultData(dataResponse);
                log.debug("Data Response :- {}",dataResponse);
                return resultSet;
            }
        }

    }

    public void connect(String user, String database) throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException {

    }

    @Override
    public void terminate() throws IOException {
            IConnectionPipeV2 connectionPipeV2 = new ConnectionPipeV2(
                    new ChannelProtocolWritableImpl(getChannel()));

            connectionPipeV2.sendTerminate(new Terminate());
    }

    public IConnection createClientSocket(ConnectionParams connectionParams) throws IOException {

        log.info("Trying to create Client Socket");
        clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(true);

        if (!clientChannel.connect(new InetSocketAddress(connectionParams.getHost(), connectionParams.getPort()))) {
            log.info("Trying to connect ...");
            while (!clientChannel.finishConnect());

            log.info("Connection Successful....");
        }
        return this;
    }

    public void initiateHandshake(String user, String database) throws InterruptedException, EdgeDBInternalErrException, EdgeDBIncompatibleDriverException, IOException {
        log.info("Initiating Client Handshake");
        ClientHandshake clientHandshakeMessage = new ClientHandshake(user,database);
        IConnectionPipeV2 connectionPipeV2 = new ConnectionPipeV2(
                new ChannelProtocolWritableImpl(getChannel()));
        connectionPipeV2.sendClientHandshake(clientHandshakeMessage);

    }

    public void handleHandshake() throws InterruptedException, EdgeDBInternalErrException, EdgeDBIncompatibleDriverException, IOException {
        tryHandleHandshake();
        log.info("Connection Successful, Ready for command.");
    }
    private <T extends ServerProtocolBehaviour> void tryHandleHandshake() throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException {
        log.debug("Trying to read response for client handshake");

        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();
        BufferReader bufferReader = new BufferReaderImpl(getChannel());
        readBuffer = bufferReader.read(readBuffer);
        while (readBuffer.hasRemaining()) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);

            T response = reader.read(readBuffer);
            log.info(" Response Found was {}",response.toString());
            if (response instanceof ServerHandshakeBehaviour) {
                ServerHandshakeBehaviour serverHandshake = (ServerHandshakeBehaviour) response;
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
                        SyncPipe syncPipe = new SyncPipeImpl(
                                new ChannelProtocolWritableImpl(getChannel()));
                        syncPipe.sendSyncMessage();
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
    public SocketChannel getChannel() {
        return this.clientChannel;
    }
}
