package edgedb.internal.protocol.server;

import lombok.Data;

@Data
public class AuthenticationOK extends BaseServerProtocol {
    byte mType = 'R';
    int messageLength;
    int authStatus;
}