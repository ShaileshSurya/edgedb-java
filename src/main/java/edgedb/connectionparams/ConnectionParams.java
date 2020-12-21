package edgedb.connectionparams;

import lombok.Data;

import java.net.URI;

@Data
public class ConnectionParams {

    private String dsn;
    private String host;
    private int port;
    private String admin;
    private String user;
    private String password;
    private String database;
    private int timeout;

    public ConnectionParams(String dsn) {
        URI uri = URI.create(dsn);
        user = uri.getUserInfo();
        database = "tutorial";
        host = uri.getHost();
        port = uri.getPort();
    }
}
