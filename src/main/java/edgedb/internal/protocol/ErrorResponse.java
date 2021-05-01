package edgedb.internal.protocol;

import lombok.Data;

import java.util.Arrays;

@Data
public class ErrorResponse implements ServerProtocolBehaviour {
    byte mType = 'E';
    int messageLength;
    byte severity;
    int errorCode;
    String message;
    short headerAttributeLength;
    Header[] header;

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "mType=" + mType +
                ", messageLength=" + messageLength +
                ", severity=" + severity +
                ", errorCodeHex=" + Integer.toHexString(errorCode) +
                ", errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", headerAttributeLength=" + headerAttributeLength +
                ", header=" + Arrays.toString(header) +
                '}';
    }
}
