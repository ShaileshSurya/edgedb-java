package edgedb.exceptions;

public class EdgeDBException extends RuntimeException{
    public EdgeDBException(Throwable e) {
        super(e);
    }

    public EdgeDBException(String message) {
        super(message);
    }
}
