package edgedb.exceptions;

public class EdgeDBCommandException extends EdgeDBClientException {

    public EdgeDBCommandException(Throwable e) {
        super(e);
    }
}
