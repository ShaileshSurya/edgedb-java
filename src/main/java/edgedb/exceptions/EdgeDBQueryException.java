package edgedb.exceptions;

public class EdgeDBQueryException extends BaseException {
    public EdgeDBQueryException(String errorCode){
        super(errorCode);
    }
}
