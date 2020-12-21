package edgedb.internal.protocol;

import lombok.Data;

@Data
public class DataElement implements ServerProtocolBehaviour {
    int dataLength;
    byte[] dataElement;
    byte[] dataElementInBinary;
    String dataElementInString;
    String[] dataElementInStringArray;
}
