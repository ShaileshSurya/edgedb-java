package edgedb.protocol.read;

public class ServerKeyData {
    byte mType;
    int messageLength;
    byte data;

    @Override
    public String toString() {
        return "ServerKeyData{" +
                "mType=" + mType +
                ", messageLength=" + messageLength +
                ", data=" + data +
                '}';
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public byte getData() {
        return data;
    }

    public void setData(byte data) {
        this.data = data;
    }
}
