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

    String dsnNew = "edgedb://edgedb@localhost:10703/edgedb";
    @Test
    public void TestGetConnection() {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());
        try {


            clientV2.getConnection(dsnNew);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EdgeDBIncompatibleDriverException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (EdgeDBInternalErrException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestTerminateConnection() {
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());
        try {
            clientV2.getConnection(dsnNew);
            clientV2.terminateConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EdgeDBIncompatibleDriverException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (EdgeDBInternalErrException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void TestGranularFlow(){
        EdgeDBClientV2 clientV2 = new EdgeDBClientV2(new BlockingConnection());

        try{
            ConnectionParams connectionParams = ConnectionParams.builder()
                    .host("localhost")
                    .port(10714)
                    .database("edgedb")
                    .user("edgedb")
                    .password("7ZTpjvjkGxIxy8FbYiabLNGf")
                    .build();

            IConnection connection = clientV2.getConnection(connectionParams);
            ResultSet result = connection.queryJSON("SELECT Movie { id, title, year }");
            log.info("Result ~~~~~{}",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}