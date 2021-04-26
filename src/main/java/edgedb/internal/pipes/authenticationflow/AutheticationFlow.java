package edgedb.internal.pipes.authenticationflow;

import com.google.common.hash.Hashing;
import edgedb.exceptions.EdgeDBUnsupportedAuthenticationMethod;
import edgedb.internal.protocol.AuthenticationSASLInitialResponse;
import edgedb.internal.protocol.client.writerV2.ProtocolWritable;
import edgedb.internal.protocol.client.writerV2.SyncBufferWritableDecorator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
@Slf4j
public class AutheticationFlow implements IAuthenticationFlow {

    public static final String SCRAM_SHA_256 = "SCRAM-SHA-256";
    ProtocolWritable protocolWritable;

    @Override
    public void sendAuthenticationSASLInitialResponseMessage(String[] methods, String... args) throws IOException {
        // Only one method is supported that is "SCRAM-SHA-256"
        if(!methods[0].equals(SCRAM_SHA_256)){
            throw new EdgeDBUnsupportedAuthenticationMethod();
        }

        final byte[] hashed = Hashing.sha256()
                .hashString(args[0], StandardCharsets.UTF_8)
                .asBytes();

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        for (byte val:hashed) {
            System.out.print((int)val);
        }

        AuthenticationSASLInitialResponse initialResponse = new AuthenticationSASLInitialResponse(SCRAM_SHA_256,hashed);
        protocolWritable.write(new SyncBufferWritableDecorator<>(initialResponse));
    }

    @Override
    public void sendAuthenticationSASLResponseMessage() {

    }
}
