package edgedb.client;

import edgedb.exceptions.FailedToConnectEdgeDBServer;
import edgedb.exceptions.FailedToDecodeServerResponseException;

import java.io.IOException;

public class ReadMeExample {

    public static void main(String[] args) {
        String dsn = "edgedb://edgedb@localhost:5656/tutorial";
        EdgeDBClient db = new EdgeDBClient().withDSN(dsn);

        try {
            db.connect();
            String jsonResult = db.execute("SELECT Movie { id, title}");
            db.terminate();
        } catch (FailedToDecodeServerResponseException |FailedToConnectEdgeDBServer| IOException e) {
            e.printStackTrace();
        }
    }
}
