package edgedb.connection;

import edgedb.client.ResultSet;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.*;
import edgedb.internal.pipes.granularflow.GranularFlowPipeV2;
import edgedb.internal.pipes.granularflow.IGranularFlowPipe;
import edgedb.internal.protocol.client.Execute;
import edgedb.internal.protocol.client.Prepare;
import edgedb.internal.protocol.client.writerV2.ChannelProtocolWriterImpl;
import edgedb.internal.protocol.server.*;
import edgedb.internal.protocol.typedescriptor.TypeDescriptor;
import edgedb.internal.protocol.typedescriptor.decoder.TypeDecoderFactoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

@Slf4j
public abstract class BaseConnection implements IConnection{
    SocketChannel clientChannel;
    byte[] serverKey;
    boolean readyForCommand = false;
    @Override
    public IConnection connect(ConnectionParams connectionParams) throws IOException {
        clientChannel = SocketChannel.open();

        clientChannel.configureBlocking(isBlocking());

        if (!clientChannel.connect(new InetSocketAddress(connectionParams.getHost(), connectionParams.getPort()))) {
            log.info("Trying to connect ...");
            while (!clientChannel.finishConnect());

            log.info("Connection Successful....");
        }
        return getConnection();
    }

    @Override
    public void terminate() throws IOException {
        log.info("Trying to Disconnect ...");
        clientChannel.close();
    }

    protected abstract IConnection getConnection();

    protected abstract boolean isBlocking();

    public SocketChannel getChannel(){
        return this.clientChannel;
    }


}
