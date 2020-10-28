package edgedb.protocol.server;

import lombok.Data;

@Data
public class AuthenticationSASL extends BaseServerProtocol{
    byte mType = (int) 'R';
    int messageLength;
    int authStatus;
    int numMethods;
}
