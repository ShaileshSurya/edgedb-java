package edgedb.internal.pipes.authenticationflow;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.pipes.authenticationflow.scram.client.ScramClientFunctionality;
import edgedb.internal.pipes.authenticationflow.scram.client.ScramClientFunctionalityImpl;
import edgedb.internal.pipes.authenticationflow.scram.common.ScramException;
import edgedb.internal.protocol.AuthenticationSASLClientFinalResponse;
import edgedb.internal.protocol.AuthenticationSASLInitialResponse;
import edgedb.internal.protocol.ServerAuthenticationBehaviour;
import edgedb.internal.protocol.client.writerV2.ProtocolWritable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_CREATE_CLIENT_FINAL_MESSAGE;
import static edgedb.exceptions.ErrorMessage.FAILED_TO_CREATE_CLIENT_FIRST_MESSAGE;

@AllArgsConstructor
@Slf4j
public class AutheticationFlowScramSASL implements IScramSASLAuthenticationFlow {

    public static final String SCRAM_SHA_256 = "SCRAM-SHA-256";
    public static final String DIGEST_NAME = "SHA-256";
    public static final String HMAC_NAME = "HmacSHA256";

    public String clientNonce;
    ProtocolWritable protocolWritable;
    ScramClientFunctionality scramClientFunctionality;

    public AutheticationFlowScramSASL(ProtocolWritable protocolWritable) {
        clientNonce = UUID.randomUUID().toString();
        this.protocolWritable = protocolWritable;
        scramClientFunctionality = new ScramClientFunctionalityImpl(DIGEST_NAME, HMAC_NAME);
    }


    @Override
    public void sendAuthenticationSASLClientFirstMessage(String username) throws IOException, EdgeDBInternalErrException {
        // Only one method is supported that is "SCRAM-SHA-256"
        try {
            String clientFirstMessage = scramClientFunctionality.prepareFirstMessage(username);

            AuthenticationSASLInitialResponse initialResponse = new AuthenticationSASLInitialResponse(SCRAM_SHA_256, clientFirstMessage.getBytes("UTF-8"));
            log.info("Trying to write SASLInitialResponseMessage {}", initialResponse);
            protocolWritable.write(initialResponse);
        } catch (ScramException e) {
            log.error("error {}", e);
            throw new EdgeDBInternalErrException(FAILED_TO_CREATE_CLIENT_FIRST_MESSAGE);
        }
    }

    @Override
    public void sendAuthenticationSASLClientFinalMessage(ServerAuthenticationBehaviour serverAuthenticationSASLContinue, String password) throws EdgeDBInternalErrException, IOException {

        try {
            String serverFirstMessage = new String(serverAuthenticationSASLContinue.getSaslData());
            String clientFinalMessage = scramClientFunctionality.prepareFinalMessage(password, serverFirstMessage);

            AuthenticationSASLClientFinalResponse finalResponse = new AuthenticationSASLClientFinalResponse(clientFinalMessage.getBytes());
            //AuthenticationSASLInitialResponse initialResponse = new Authentication(SCRAM_SHA_256, clientFirstMessage.getBytes("UTF-8"));
            log.info("Trying to write SASLFinalResponseMessage {}", finalResponse);
            protocolWritable.write(finalResponse);
        }catch (ScramException e){
            log.error("error {}", e);
            throw new EdgeDBInternalErrException(FAILED_TO_CREATE_CLIENT_FINAL_MESSAGE);
        }
    }
}
