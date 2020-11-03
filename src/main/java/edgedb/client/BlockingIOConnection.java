package edgedb.client;


public class BlockingIOConnection extends BaseConnection {

    public BlockingIOConnection(String dsn){
        super(dsn);
    }

    @Override
    public void connect(String dsn) {

    }

    @Override
    public void terminate() {

    }
}
