package edgedb.protocol.write;

public class Execute {
    byte mType = (int)'E';
    int messageLength;
    short headersLength;
    Header[] headers;
    byte[] statementName;
    byte[] arguments;

    public byte getmType() {
        return mType;
    }

    public void setmType(byte mType) {
        this.mType = mType;
    }

    public int getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

    public short getHeadersLength() {
        return headersLength;
    }

    public void setHeadersLength(short headersLength) {
        this.headersLength = headersLength;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public byte[] getStatementName() {
        return statementName;
    }

    public void setStatementName(byte[] statementName) {
        this.statementName = statementName;
    }

    public byte[] getArguments() {
        return arguments;
    }

    public void setArguments(byte[] arguments) {
        this.arguments = arguments;
    }
}
