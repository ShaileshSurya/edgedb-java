package edgedb.internal.protocol;

import lombok.Data;

@Data
public class AuthenticationSASL implements ServerProtocolBehaviour {
    byte mType = 'R';
    int messageLength;
    int authStatus;
    int numMethods;
    String[] methods;
}
