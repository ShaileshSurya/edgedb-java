package edgedb.connection;

import edgedb.client.ResultSet;
import edgedb.client.ResultSetImpl;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.*;
import edgedb.internal.buffer.SingletonBuffer;
import edgedb.internal.pipes.SyncFlow.SyncPipe;
import edgedb.internal.pipes.SyncFlow.SyncPipeImpl;
import edgedb.internal.pipes.connect.ConnectionPipeV3;
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
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_TRANSACTION_STATE;
import static edgedb.internal.protocol.constants.TransactionState.*;

@Slf4j
public class NonBlockingConnection implements IConnection {

    SocketChannel clientChannel;
    Selector selector;
    byte[] serverKey;

    protected boolean isBlocking() {
        return false;
    }

    public IConnection connect(ConnectionParams connectionParams) throws IOException, EdgeDBInternalErrException {
        clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);

        if (!clientChannel.connect(new InetSocketAddress(connectionParams.getHost(), connectionParams.getPort()))) {
            log.info("Trying to connect ...");
            while (!clientChannel.finishConnect());

            log.info("Connection Successful....");
        }

        selector = Selector.open();
        SelectionKey readKey = clientChannel.register(selector, SelectionKey.OP_READ);
        SelectionKey writeKey = clientChannel.register(selector, SelectionKey.OP_WRITE);
        return this;
    }

    public void handleHandshake(){
        ConnectionPipeV3 connectionPipe = new ConnectionPipeV3(this.clientChannel);

    }

    public <T extends ServerProtocolBehaviour> void  handleChannelRead(SelectionKey key) throws IOException, EdgeDBInternalErrException {
        SocketChannel channel= (SocketChannel)key.channel();
        BufferReader bufferReader = new BufferReaderImpl(getChannel());
        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();;
        readBuffer = bufferReader.read(readBuffer);
        byte mType = readBuffer.get();
        ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                .getProtocolReader((char) mType, readBuffer);
        T response = reader.read(readBuffer);

    }

    @Override
    public IConnection createClientSocket(ConnectionParams connectionParams) throws IOException {
        return null;
    }

    @Override
    public void terminate() throws IOException {

    }

    @Override
    public void initiateHandshake(String user, String database) throws EdgeDBInternalErrException, InterruptedException, EdgeDBIncompatibleDriverException, IOException {

    }

    @Override
    public ResultSet query(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.BINARY, Cardinality.MANY, query);
    }

    @Override
    public ResultSet queryOne(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet queryJSON(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet execute(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        throw new UnsupportedOperationException();
    }


    protected  <T extends ServerProtocolBehaviour> PrepareComplete readPrepareComplete(IGranularFlowPipe granularFlowPipe, Prepare prepareMessage) throws IOException, EdgeDBInternalErrException, EdgeDBCommandException {
        log.debug("Reading prepare complete");
        BufferReader bufferReader = new BufferReaderImpl(getChannel());
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
                        SyncPipe syncPipe = new SyncPipeImpl(
                                new ChannelProtocolWritableImpl(getChannel()));
                        syncPipe.sendSyncMessage();
                        continue;
                    case (int) IN_TRANSACTION:
                        throw new UnsupportedOperationException();
                    case (int) NOT_IN_TRANSACTION:
                        log.info("Not In Transaction");
//                        granularFlowPipe.sendPrepareMessage(prepareMessage);
//                        return readPrepareComplete(granularFlowPipe,prepareMessage);
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

    @Override
    public SocketChannel getChannel() {
        return null;
    }
}
