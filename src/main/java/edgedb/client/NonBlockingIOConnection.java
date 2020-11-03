package edgedb.client;

public class NonBlockingIOConnection extends BaseConnection{

    protected NonBlockingIOConnection(String dsn) {
        super(dsn);
    }

    @Override
    public void connect(String dsn) {

    }

    @Override
    public void terminate() {

    }
}
