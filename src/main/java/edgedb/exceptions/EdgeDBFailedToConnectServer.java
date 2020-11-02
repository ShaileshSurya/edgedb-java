package edgedb.exceptions;

public class EdgeDBFailedToConnectServer extends EdgeDBClientException {

    public EdgeDBFailedToConnectServer(Throwable e) {
        super(e);
    }
}
