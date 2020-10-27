package edgedb.protocol.server;

import lombok.Data;

@Data
public class DataElement {
    int dataLength;
    byte[] dataElement;
    byte[] dataElementInBinary;
    String dataElementInString;
    String[] dataElementInStringArray;
}
