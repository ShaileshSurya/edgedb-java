package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;
import lombok.Data;

@Data
public class InvalidArgumentException extends BaseException {
    public InvalidArgumentException(String errorCode){
        super(errorCode);
    }
}
