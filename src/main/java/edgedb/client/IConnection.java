package edgedb.client;

import java.io.IOException;

public interface IConnection<C extends Connection> {
    public void connect() throws IOException;
    public void terminate();
}
