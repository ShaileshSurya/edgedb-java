package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;

import javax.print.attribute.standard.Severity;

public class ClientConnectionTimeoutException extends BaseException {

    public static final String errorCode = "ClientConnectionTimeoutError";

    public static final String message = "ClientConnectionTimeoutError";

   // public static final Severity severity = ;
    public ClientConnectionTimeoutException(String errorCode, String message, String severity) {
        super("ClientConnectionTimeoutError",message,severity);
    }
}
