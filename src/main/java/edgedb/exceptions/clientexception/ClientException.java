package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class ClientException extends BaseException {

    public ClientException(String errorCode){
        super(errorCode);
    }
}
