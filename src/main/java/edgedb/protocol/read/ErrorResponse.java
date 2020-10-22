package edgedb.protocol.read;

public class ErrorResponse {
    byte mType= (byte) 'E';
    int messageLength;

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "mType=" + mType +
                ", messageLength=" + messageLength +
                ", severity=" + severity +
                ", errorCode=" + errorCode +
                ", message='" + message + '\'' +
                ", headerAttributeLength=" + headerAttributeLength +
                '}';
    }

    byte severity;

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

    public byte getSeverity() {
        return severity;
    }

    public void setSeverity(byte severity) {
        this.severity = severity;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public short getHeaderAttributeLength() {
        return headerAttributeLength;
    }

    public void setHeaderAttributeLength(short headerAttributeLength) {
        this.headerAttributeLength = headerAttributeLength;
    }

    int errorCode;
    String message;
    short headerAttributeLength;
    //  Header          attributes[num_attributes];
}
