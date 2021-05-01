package edgedb.client;


import edgedb.connection.BlockingConnection;
import edgedb.connection.IConnection;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class EdgeDBClientV2Test {

    @Test
    public void TestGetConnection() {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());
        try {
            clientV2.getConnection(new ConnectionParams());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestTerminateConnection() {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());
        try {
            clientV2.getConnection(new ConnectionParams());
            clientV2.terminateConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void TestGranularFlow(){
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());

        try{
            IConnection connection = clientV2.getConnection(new ConnectionParams());
            ResultSet result = connection.queryJSON("SELECT Movie { id, title, year }");
            log.info("Result ~~~~~{}",result);
        } catch (EdgeDBInternalErrException e) {
            e.printStackTrace();
        } catch (EdgeDBCommandException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EdgeDBQueryException e) {
            e.printStackTrace();
        }
    }
}