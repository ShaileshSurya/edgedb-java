package edgedb.exceptions;

public class EdgeDBFailedToConnectServer extends EdgeDBClientException {

    public EdgeDBFailedToConnectServer(Throwable e) {
        super(e);
    }

    public EdgeDBFailedToConnectServer(String msg){
        super(msg);
    }
}
