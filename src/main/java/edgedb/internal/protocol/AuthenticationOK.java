package edgedb.internal.protocol;

import lombok.Data;

@Data
public class AuthenticationOK implements ServerProtocolBehaviour {
    byte mType = 'R';
    int messageLength;
    int authStatus;
}
