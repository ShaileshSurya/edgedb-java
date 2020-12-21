package edgedb.connection;

import edgedb.client.ResultSet;
import edgedb.client.ResultSetImpl;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.*;
import edgedb.internal.buffer.SingletonBuffer;
import edgedb.internal.pipes.SyncFlow.SyncPipe;
import edgedb.internal.pipes.SyncFlow.SyncPipeImpl;
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


import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_TRANSACTION_STATE;
import static edgedb.internal.protocol.constants.TransactionState.*;

@Slf4j
public class NonBlockingConnection implements IConnection {

    SocketChannel clientChannel;
    Selector selector;
    byte[] serverKey;


    public void handleHandshake(){

    }


    @Override
    public IConnection createClientSocket(ConnectionParams connectionParams) throws IOException {
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

    @Override
    public void terminate() throws IOException {

    }

    @Override
    public void initiateHandshake(String user, String database) throws EdgeDBInternalErrException, InterruptedException, EdgeDBIncompatibleDriverException, IOException {

    }

    @Override
    public ResultSet query(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        throw new UnsupportedOperationException();
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

    @Override
    public SocketChannel getChannel() {
        return null;
    }
}
