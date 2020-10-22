package edgedb.protocol.read;


public class AuthenticationOK {
    byte mType = (int)'R';
    int messageLength;
    int authStatus;

    @Override
    public String toString() {
        return "AuthenticationOK{" +
                "mType=" + mType +
                ", messageLength=" + messageLength +
                ", authStatus=" + authStatus +
                '}';
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }
}
