package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class InvalidArgumentException extends BaseException {
    public InvalidArgumentException(String errorCode){
        super(errorCode);
    }
}
