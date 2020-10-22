package edgedb.protocol.client;

import lombok.Data;

@Data
public class ExecuteScript {
    byte mType = (int)'Q';
    int messageLength;
    short headersLength;
    String script;
}
