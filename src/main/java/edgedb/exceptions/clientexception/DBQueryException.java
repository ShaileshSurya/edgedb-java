package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;
import lombok.Data;

@Data
public class DBQueryException extends BaseException {

    public DBQueryException(String errorCode){
        super(errorCode);
    }
}
