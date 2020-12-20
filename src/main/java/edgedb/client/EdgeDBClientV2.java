package edgedb.client;

import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.EdgeDBIncompatibleDriverException;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.pipes.connect.ConnectionPipeV3;
import edgedb.internal.pipes.connect.IConnectionPipe;
import edgedb.internal.protocol.client.writerV2.ChannelProtocolWriterImpl;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class EdgeDBClientV2 implements Client{

    IConnection connection;
    ConnectionParams connectionParams;

    public EdgeDBClientV2(IConnection connection){
        this.connection = connection;
    }


    @Override
    public IConnection getConnection(String dsn) throws IOException, InterruptedException, EdgeDBIncompatibleDriverException, EdgeDBInternalErrException {
        connectionParams = new ConnectionParams(dsn);
        connection = connection.connect(connectionParams);
        IConnectionPipe pipe = new ConnectionPipeV3(connection.getChannel());
        pipe.connect(connectionParams.getUser(),connectionParams.getDatabase());
        return connection;
    }


    @Override
    public void terminateConnection() throws IOException {
        IConnectionPipe pipe= new ConnectionPipeV3(connection.getChannel());
        pipe.terminate();
        connection.terminate();
    }

}
