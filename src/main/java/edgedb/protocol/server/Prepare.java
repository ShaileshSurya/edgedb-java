package edgedb.protocol.server;

import edgedb.protocol.client.Header;
import lombok.Data;

@Data
public class Prepare {
    byte mType = 'P';
    int messageLength;
    short headersLength;
    Header[] headers;
    byte ioFormat;
    byte expectedCardinality;
    byte[] statement;
    String command;
}
