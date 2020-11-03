package edgedb.client;

import lombok.Data;

import java.net.URI;

@Data
public abstract class BaseConnection {
    private String dsn;
    private String host;
    private int port;
    private String admin;
    private String user;
    private String password;
    private String database;
    private int timeout;

    protected BaseConnection(String dsn) {
        URI uri = URI.create(dsn);
        user = uri.getUserInfo();
        // TODO: remove this hardcoded
        database = "tutorial";
        host = uri.getHost();
        port = uri.getPort();
    }

    abstract public void connect(String dsn);
    abstract public void terminate();
}
