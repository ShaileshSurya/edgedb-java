package edgedb.internal.protocol;

public class AuthenticationSASLContinue {

    byte mType = 'R';
    int messageLength;
    int authStatus;
    byte[] saslData;
}
