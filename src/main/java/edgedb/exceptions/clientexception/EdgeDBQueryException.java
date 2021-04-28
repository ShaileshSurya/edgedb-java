package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class EdgeDBQueryException extends BaseException {

    public EdgeDBQueryException(String errorCode){
        super(errorCode);
    }
}
