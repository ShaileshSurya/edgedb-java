package edgedb.protocol.server;

import lombok.Data;

@Data
public class AuthenticationOK extends BaseServerProtocol {
    byte mType = (int)'R';
    int messageLength;
    int authStatus;
}
