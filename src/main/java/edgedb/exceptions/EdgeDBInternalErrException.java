package edgedb.exceptions;

public class EdgeDBInternalErrException extends EdgeDBException {
    public EdgeDBInternalErrException(Throwable e) {
        super(e);
    }

    public EdgeDBInternalErrException(String message) {
        super(message);
    }
}
