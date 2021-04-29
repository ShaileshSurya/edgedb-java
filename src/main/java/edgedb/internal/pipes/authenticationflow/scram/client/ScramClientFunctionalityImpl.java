package edgedb.internal.pipes.authenticationflow.scram.client;

import edgedb.internal.pipes.authenticationflow.scram.common.Base64;
import edgedb.internal.pipes.authenticationflow.scram.common.ScramException;
import edgedb.internal.pipes.authenticationflow.scram.common.ScramUtils;
import edgedb.internal.pipes.authenticationflow.scram.common.StringPrep;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Provides building blocks for creating SCRAM authentication client
 */
@Data
@Slf4j
@SuppressWarnings("unused")
public class ScramClientFunctionalityImpl implements ScramClientFunctionality {
    private static final Pattern SERVER_FIRST_MESSAGE = Pattern.compile("r=([^,]*),s=([^,]*),i=(.*)$");
    private static final Pattern SERVER_FINAL_MESSAGE = Pattern.compile("v=([^,]*)$");

    private static final String GS2_HEADER = "n,,";
    private static final Charset ASCII = Charset.forName("ASCII");

    private final String mDigestName;
    private final String mHmacName;
    private final String mClientNonce;
    private String mClientFirstMessageBare;

    private boolean mIsSuccessful = false;
    private byte[] mSaltedPassword;
    private String mAuthMessage;

    private State mState = State.INITIAL;


    /**
     * Create new ScramClientFunctionalityImpl
     *
     * @param digestName Digest to be used
     * @param hmacName   HMAC to be used
     */
    public ScramClientFunctionalityImpl(String digestName, String hmacName) {
        this(digestName, hmacName, UUID.randomUUID().toString());
    }


    /**
     * Create new ScramClientFunctionalityImpl
     *
     * @param digestName  Digest to be used
     * @param hmacName    HMAC to be used
     * @param clientNonce Client nonce to be used
     */
    public ScramClientFunctionalityImpl(String digestName, String hmacName, String clientNonce) {
        if (ScramUtils.isNullOrEmpty(digestName)) {
            throw new NullPointerException("digestName cannot be null or empty");
        }
        if (ScramUtils.isNullOrEmpty(hmacName)) {
            throw new NullPointerException("hmacName cannot be null or empty");
        }
        if (ScramUtils.isNullOrEmpty(clientNonce)) {
            throw new NullPointerException("clientNonce cannot be null or empty");
        }

        mDigestName = digestName;
        mHmacName = hmacName;
        mClientNonce = clientNonce;
    }


    /**
     * Prepares first client message
     * <p>
     * You may want to use {@link StringPrep#isContainingProhibitedCharacters(String)} in order to check if the
     * username contains only valid characters
     *
     * @param username Username
     * @return prepared first message
     * @throws ScramException if <code>username</code> contains prohibited characters
     */
    @Override
    public String prepareFirstMessage(String username) throws ScramException {
        if (mState != State.INITIAL) {
            throw new IllegalStateException("You can call this method only once");
        }

        try {
            mClientFirstMessageBare = "n=" + StringPrep.prepAsQueryString(username) + ",r=" + mClientNonce;
            mState = State.FIRST_PREPARED;
            return GS2_HEADER + mClientFirstMessageBare;
        } catch (StringPrep.StringPrepError e) {
            mState = State.ENDED;
            throw new ScramException("Username contains prohibited character");
        }
    }


    @Override
    public String prepareFinalMessage(String password, String serverFirstMessage) throws ScramException {
        if (mState != State.FIRST_PREPARED) {
            throw new IllegalStateException("You can call this method once only after " +
                    "calling prepareFirstMessage()");
        }

        Matcher m = SERVER_FIRST_MESSAGE.matcher(serverFirstMessage);
        if (!m.matches()) {
            mState = State.ENDED;
            return null;
        }

        String nonce = m.group(1);

        if (!nonce.startsWith(mClientNonce)) {
            mState = State.ENDED;
            return null;
        }

        String salt = m.group(2);
        String iterationCountString = m.group(3);
        int iterations = Integer.parseInt(iterationCountString);
        if (iterations <= 0) {
            mState = State.ENDED;
            return null;
        }

        try {
            mSaltedPassword = ScramUtils.generateSaltedPassword(password,
                    Base64.decode(salt),
                    iterations,
                    mHmacName);


            String clientFinalMessageWithoutProof = "c=" + Base64.encodeBytes(GS2_HEADER.getBytes(ASCII)
                    , Base64.DONT_BREAK_LINES)
                    + ",r=" + nonce;

            mAuthMessage = mClientFirstMessageBare + "," + serverFirstMessage + "," + clientFinalMessageWithoutProof;

            byte[] clientKey = ScramUtils.computeHmac(mSaltedPassword, mHmacName, "Client Key");
            byte[] storedKey = MessageDigest.getInstance(mDigestName).digest(clientKey);

            byte[] clientSignature = ScramUtils.computeHmac(storedKey, mHmacName, mAuthMessage);

            byte[] clientProof = clientKey.clone();
            for (int i = 0; i < clientProof.length; i++) {
                clientProof[i] ^= clientSignature[i];
            }

            mState = State.FINAL_PREPARED;
            return clientFinalMessageWithoutProof + ",p=" + Base64.encodeBytes(clientProof, Base64.DONT_BREAK_LINES);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            mState = State.ENDED;
            throw new ScramException(e);
        }
    }


    @Override
    public boolean checkServerFinalMessage(String serverFinalMessage) throws ScramException {
        if (mState != State.FINAL_PREPARED) {
            throw new IllegalStateException("You can call this method only once after " +
                    "calling prepareFinalMessage()");
        }

        Matcher m = SERVER_FINAL_MESSAGE.matcher(serverFinalMessage);
        if (!m.matches()) {
            mState = State.ENDED;
            return false;
        }

        byte[] serverSignature = Base64.decode(m.group(1));

        mState = State.ENDED;
        mIsSuccessful = Arrays.equals(serverSignature, getExpectedServerSignature());

        return mIsSuccessful;
    }


    @Override
    public boolean isSuccessful() {
        if (mState == State.ENDED) {
            return mIsSuccessful;
        } else {
            throw new IllegalStateException("You cannot call this method before authentication is ended. " +
                    "Use isEnded() to check that");
        }
    }


    @Override
    public boolean isEnded() {
        return mState == State.ENDED;
    }


    @Override
    public State getState() {
        return mState;
    }


    private byte[] getExpectedServerSignature() throws ScramException {
        try {
            byte[] serverKey = ScramUtils.computeHmac(mSaltedPassword, mHmacName, "Server Key");
            return ScramUtils.computeHmac(serverKey, mHmacName, mAuthMessage);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            mState = State.ENDED;
            throw new ScramException(e);
        }
    }
}
