package edgedb.internal.pipes.authenticationflow;

import java.io.IOException;

public interface  IAuthenticationFlow {
    public void sendAuthenticationSASLInitialResponseMessage(String[] methods, String... args) throws IOException;
    public void sendAuthenticationSASLResponseMessage();
}
