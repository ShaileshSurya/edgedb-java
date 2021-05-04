package edgedb.internal.pipes.authenticationflow;

import edgedb.internal.protocol.ServerAuthenticationBehaviour;

import java.io.IOException;

public interface IScramSASLAuthenticationFlow {
    void sendAuthenticationSASLClientFirstMessage(String username) throws IOException;
    void sendAuthenticationSASLClientFinalMessage(ServerAuthenticationBehaviour serverAuthenticationSASLContinue, String password) throws IOException;
}
