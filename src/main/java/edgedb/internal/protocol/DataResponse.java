package edgedb.internal.protocol;

import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.DATA_RESPONSE;

@Data
public class DataResponse implements ServerProtocolBehaviour {
    byte mType = DATA_RESPONSE;
    int messageLength;

    int reserved;

    short dataLength;
    DataElement[] dataElements;
}
