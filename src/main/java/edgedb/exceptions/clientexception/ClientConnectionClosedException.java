package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class ClientConnectionClosedException extends BaseException {

    public ClientConnectionClosedException(String errorCode) {
        super(errorCode);
    }

    public ClientConnectionClosedException(String errorCode, String message, String severity) {
        super(errorCode,message,severity);
    }
}
