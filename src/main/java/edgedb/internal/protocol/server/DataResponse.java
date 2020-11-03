package edgedb.internal.protocol.server;

import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.DATA_RESPONSE;

@Data
public class DataResponse extends BaseServerProtocol {
    byte mType = DATA_RESPONSE;
    int messageLength;

    int reserved;

    short dataLength;
    DataElement[] dataElements;
}
