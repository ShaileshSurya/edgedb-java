package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

public class ClientConnectionFailedException extends BaseException {

    public ClientConnectionFailedException(String errorCode) {
        super(errorCode);
    }

    public ClientConnectionFailedException(String errorCode, String message, String severity) {
        super(errorCode,message,severity);
    }
}
