package edgedb.protocol.read;

public class AuthenticationSASL {
    byte mType = (int)'R';
    int messageLength;
    int authStatus;
    int numMethods;

    @Override
    public String toString() {
        return "AuthenticationSASL{" +
                "mType=" + mType +
                ", messageLength=" + messageLength +
                ", authStatus=" + authStatus +
                ", numMethods=" + numMethods +
                '}';
    }

    public int getNumMethods() {
        return numMethods;
    }

    public void setNumMethods(int numMethods) {
        this.numMethods = numMethods;
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
