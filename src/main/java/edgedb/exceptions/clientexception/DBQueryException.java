package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class DBQueryException extends BaseException {

    public DBQueryException(String errorCode){
        super(errorCode);
    }
}
