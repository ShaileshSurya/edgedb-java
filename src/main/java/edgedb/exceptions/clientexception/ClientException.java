package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;
import lombok.Data;

@Data
public class ClientException extends BaseException {

    public ClientException(String errorCode){
        super(errorCode);
    }
}
