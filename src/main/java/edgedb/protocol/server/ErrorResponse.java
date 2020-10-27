package edgedb.protocol.server;

import lombok.Data;

@Data
public class ErrorResponse {
    byte mType = (byte) 'E';
    int messageLength;
    int errorCode;
    String message;
    short headerAttributeLength;
    //  Header          attributes[num_attributes];
}
