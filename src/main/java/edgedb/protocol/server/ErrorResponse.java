package edgedb.protocol.server;

import edgedb.protocol.common.Header;
import lombok.Data;

import java.util.Arrays;

@Data
public class ErrorResponse extends BaseServerProtocol {
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
                ", errorCode=" + Integer.toHexString(errorCode) +
                ", errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", headerAttributeLength=" + headerAttributeLength +
                ", header=" + Arrays.toString(header) +
                '}';
    }
}
