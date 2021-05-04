package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class ClientConnectionException extends BaseException {

    public ClientConnectionException(String errorCode) {
        super(errorCode);
    }

    public ClientConnectionException(String errorCode, String message, String severity) {
        super(errorCode,message,severity);
    }
}
