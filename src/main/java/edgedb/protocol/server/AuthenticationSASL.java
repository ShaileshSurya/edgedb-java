package edgedb.protocol.server;

import lombok.Data;

@Data
public class AuthenticationSASL {
    byte mType = (int)'R';
    int messageLength;
    int authStatus;
    int numMethods;
}
