package edgedb.client;

public abstract class BaseConnection {
    abstract public void connect(String dsn);
    abstract public void terminate();
}
