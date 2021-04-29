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

    public static BaseException getExceptionFromError(ErrorResponse errorResponse){
        Map<Byte, BaseException> errorCodesMap = ErrorCodesToExceptionMap.errorCodesMap;
        BaseException exception = errorCodesMap.get((byte)errorResponse.getErrorCode());
        exception.setMessage(errorResponse.getMessage());
        exception.setSeverity(getExceptionSeverity(errorResponse.getSeverity()));

        Optional<Header> header = Arrays.stream(errorResponse.getHeader())
                .filter(
                    head -> head.getCode() == SERVER_STACKTRACE
                )
                .findAny();

        if(header.isPresent()){
            String stackTrace = new String(header.get().getValue());
            exception.setEdgedbStackTrace(stackTrace);
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
