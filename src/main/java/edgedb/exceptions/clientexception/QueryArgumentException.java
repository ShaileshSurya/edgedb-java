package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class QueryArgumentException extends BaseException {
    public QueryArgumentException(String errorCode){
        super(errorCode);
    }
}
