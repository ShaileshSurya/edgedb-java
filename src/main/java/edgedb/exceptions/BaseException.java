package edgedb.exceptions;

import lombok.Data;

@Data
public abstract class BaseException extends RuntimeException {
    private String errorCode;
    private String severity;
    private String edgedbStackTrace;
    private String message;

    public BaseException(String errorCode){
        this.errorCode = errorCode;
    }
//    byte severity;
//    int errorCode;
//    String message;
    // TODO: We want to throw the stack trace as well.
    // TODO: How do we do that?

}

