package edgedb.internal.protocol.server;

import lombok.Data;

@Data
public class DataElement extends BaseServerProtocol {
    int dataLength;
    byte[] dataElement;
    byte[] dataElementInBinary;
    String dataElementInString;
    String[] dataElementInStringArray;
}
