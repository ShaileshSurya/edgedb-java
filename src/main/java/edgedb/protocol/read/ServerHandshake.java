package edgedb.protocol.read;

import edgedb.protocol.write.ProtocolExtension;

import java.util.Arrays;


public class ServerHandshake {
    byte mType = 'v';
    int messageLength;
    short majorVersion;
    short minorVersion;
    short protocolExtensionLength;
    ProtocolExtension[] protocolExtensions;

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

    public short getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(short majorVersion) {
        this.majorVersion = majorVersion;
    }

    public short getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(short minorVersion) {
        this.minorVersion = minorVersion;
    }

    public short getProtocolExtensionLength() {
        return protocolExtensionLength;
    }

    public void setProtocolExtensionLength(short protocolExtensionLength) {
        this.protocolExtensionLength = protocolExtensionLength;
    }

    public ProtocolExtension[] getProtocolExtensions() {
        return protocolExtensions;
    }

    public void setProtocolExtensions(ProtocolExtension[] protocolExtensions) {
        this.protocolExtensions = protocolExtensions;
    }

    @Override
    public String toString() {
        return "ServerHandshake{" +
                "mType=" + mType +
                ", messageLength=" + messageLength +
                ", majorVersion=" + majorVersion +
                ", minorVersion=" + minorVersion +
                ", protocolExtensionLength=" + protocolExtensionLength +
                ", protocolExtensions=" + Arrays.toString(protocolExtensions) +
                '}';
    }
}
