package edgedb.protocol.client;

import lombok.Data;

@Data
public class Execute {
    byte mType = (int) 'E';
    int messageLength;
    short headersLength;
    Header[] headers;
    byte[] statementName;
    byte[] arguments;
}
