package edgedb.exceptions;

public class EdgeDBClientException extends EdgeDBException {

    public EdgeDBClientException(Throwable e) {
        super(e);
    }

    public EdgeDBClientException(String message) {
        super(message);
    }
}
