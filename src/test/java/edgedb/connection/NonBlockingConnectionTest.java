package edgedb.connection;

import edgedb.connectionparams.ConnectionParams;
import org.junit.Test;


public class NonBlockingConnectionTest {

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

    public String dsnNew = "edgedb://edgedb@10.199.198.56:5656/tutorial";

//    @Test
//    public void testConnect() {
//        try {
//          new NonBlockingConnection().connect(new ConnectionParams(dsn)).terminate();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}