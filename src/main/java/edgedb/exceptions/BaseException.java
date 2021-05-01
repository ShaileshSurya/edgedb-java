package edgedb.exceptions;

import lombok.Data;

@Data
public abstract class BaseException extends RuntimeException {
    private String errorCode;
    private String severity;
    private String edgedbStackTrace;
    private String message;
    private String hint;
    private String details;

    public BaseException(String errorCode){
        this.errorCode = errorCode;
    }


}

