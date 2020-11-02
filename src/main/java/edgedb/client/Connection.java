package edgedb.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Data
@Slf4j
public class Connection {
    private String dsn;
    private String host;
    private int port;
    private String admin;
    private String user;
    private String password;
    private String database;
    private int timeout;


    /*dsn=None, *, host=None, port=None, admin=None, user=None, password=None, database=None, timeout=60*/

    public Connection(String dsn) {
        URI uri = URI.create(dsn);
        user = uri.getUserInfo();
        // TODO: remove this hardcoded
        database = "tutorial";
        host = uri.getHost();
        port = uri.getPort();
    }

}
