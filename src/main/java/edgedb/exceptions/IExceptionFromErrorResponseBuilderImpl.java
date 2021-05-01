package edgedb.exceptions;

import edgedb.internal.protocol.ErrorResponse;
import edgedb.internal.protocol.Header;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public final class IExceptionFromErrorResponseBuilderImpl {
    ErrorResponse errorResponse;

//    byte mType = 'E';
//    int messageLength;
//    byte severity;
//    int errorCode;
//    String message;
//    short headerAttributeLength;
//    Header[] header;

    private static final byte ERROR = (byte) 0x78;
    private static final byte FATAL = (byte) 0xc8;
    private static final byte PANIC = (byte) 0xff;
    private static final short SERVER_STACKTRACE = (short) 0x0101;
    private static final short HINT= (short) 0x0001;
    private static final short DETAILS= (short) 0x0002;

    public static BaseException getExceptionFromError(ErrorResponse errorResponse){
        Map<Integer, BaseException> errorCodesMap = ErrorCodesToExceptionMap.errorCodesMap;
        BaseException exception = errorCodesMap.get(errorResponse.getErrorCode());
        exception.setMessage(errorResponse.getMessage());
        exception.setSeverity(getExceptionSeverity(errorResponse.getSeverity()));

        for (Header head:errorResponse.getHeader()) {
            if(head.getCode()== SERVER_STACKTRACE){
                exception.setEdgedbStackTrace(new String(head.getValue()));
            }else if(head.getCode() == HINT){
                exception.setHint(new String(head.getValue()));
            }else if(head.getCode()== DETAILS){
                exception.setDetails(new String(head.getValue()));
            }
        }
        return exception;
    }

    private static String getExceptionSeverity(Byte severity){
        switch (severity){
            case ERROR:
                return "ERROR";
            case FATAL:
                return "FATAL";
            case PANIC:
                return "PANIC";
        }
        return "";
    }
}
