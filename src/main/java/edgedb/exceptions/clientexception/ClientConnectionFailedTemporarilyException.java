package edgedb.exceptions.clientexception;


import edgedb.exceptions.BaseException;
import edgedb.exceptions.constants.Severity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ClientConnectionFailedTemporarilyException extends BaseException {

    private final static String errorCode = "ClientConnectionFailedTemporarilyError";
    private final static String message= "Client connection temporarily failed, try again";
    private final static String severity = Severity.ERROR;


    public ClientConnectionFailedTemporarilyException(String errorCode) {
        super(errorCode);
    }

    public ClientConnectionFailedTemporarilyException() {
        super(errorCode,message,severity);
    }


}
