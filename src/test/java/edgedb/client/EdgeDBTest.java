package edgedb.client;

import edgedb.exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

public class EdgeDBTest {

    @Mock
    Connection connection;

    @Before

    @Test
    public void connectTest() {
        String user = "edgedb";
        String database = "tutorial";

        String hostEcho = "localhost";
        String portEcho = "8083";
        String hostReal = "10.199.198.56";
        String portReal = "5656";

        boolean isEcho = false;
        String port = isEcho ? portEcho : portReal;
        String host = isEcho ? hostEcho : hostReal;

        String dsn = "edgedb://" + user + "@" + host + ":" + port + "/" + database;
        EdgeDBClient db = new EdgeDBClient().withDSN(dsn);
        try {
            db.connect();

            //db.execute("SELECT Movie { id, title, year }");
            db.execute("SELECT 1 + 1");
        } catch (FailedToConnectEdgeDBServer | IOException | FailedToDecodeServerResponseException e) {
            e.printStackTrace();
        }
//        } catch (FailedToDecodeServerResponseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (EdgeDBServerException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
