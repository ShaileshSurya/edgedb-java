package edgedb.protocol.write;

public class ExecuteScript {
    byte mType = (int)'Q';
    int messageLength;
    short headersLength;
    String script;

    @Override
    public String toString() {
        return "ExecuteScript{" +
                "mType=" + mType +
                ", messageLength=" + messageLength +
                ", headersLength=" + headersLength +
                ", script='" + script + '\'' +
                '}';
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

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
