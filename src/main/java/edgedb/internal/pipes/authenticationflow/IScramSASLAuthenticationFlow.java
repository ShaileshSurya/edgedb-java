package edgedb.internal.pipes.authenticationflow;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.protocol.ServerAuthenticationBehaviour;

import java.io.IOException;

public interface IScramSASLAuthenticationFlow {
    public void sendAuthenticationSASLClientFirstMessage(String username) throws IOException, EdgeDBInternalErrException;
    public void sendAuthenticationSASLClientFinalMessage(ServerAuthenticationBehaviour serverAuthenticationSASLContinue, String password) throws EdgeDBInternalErrException, IOException;
}
