package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;
import lombok.Builder;
import lombok.Data;

@Data
public class ClientException extends BaseException {

    public ClientException(String errorCode){
        super(errorCode);
    }

    public ClientException(String errorCode, String message, String severity){
        super(errorCode, message, severity);
    }
}
