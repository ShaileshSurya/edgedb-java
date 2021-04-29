package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class UnknownArgumentException extends BaseException {
    public UnknownArgumentException(String errorCode){
        super(errorCode);
    }
}
