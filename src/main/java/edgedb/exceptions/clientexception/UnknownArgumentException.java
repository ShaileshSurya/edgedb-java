package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;
import lombok.Data;

@Data
public class UnknownArgumentException extends BaseException {
    public UnknownArgumentException(String errorCode){
        super(errorCode);
    }
}
