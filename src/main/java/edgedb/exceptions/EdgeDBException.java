package edgedb.exceptions;

public class EdgeDBException extends Exception {
    public EdgeDBException(Throwable e) {
        super(e);
    }

    public EdgeDBException(String message) {
        super(message);
    }
}
