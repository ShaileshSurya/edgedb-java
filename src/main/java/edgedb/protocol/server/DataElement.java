package edgedb.protocol.server;

import lombok.Data;

@Data
public class DataElement {
    int dataLength;
    byte[] dataElement;
}
