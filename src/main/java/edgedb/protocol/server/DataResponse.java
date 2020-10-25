package edgedb.protocol.server;

import lombok.Data;

import static edgedb.protocol.constants.MessageType.DATA_RESPONSE;

@Data
public class DataResponse {
    byte mType=DATA_RESPONSE;
    int messageLength;

    int reserved;

    short dataLength;
    DataElement[] dataElements;
}
