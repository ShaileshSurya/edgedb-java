package edgedb.exceptions.clientexception;

import edgedb.exceptions.BaseException;
import lombok.Data;

@Data
public class QueryArgumentException extends BaseException {
    public QueryArgumentException(String errorCode){
        super(errorCode);
    }
}
