package edgedb.internal.protocol.server.reader;

import edgedb.internal.protocol.server.*;

import java.io.DataInputStream;
import java.io.IOException;

@Deprecated
//TODO Remove this function
public class BaseDecoder {

    DataInputStream in;

    public BaseDecoder(DataInputStream in) {
        this.in = in;
    }

    public String decode() throws Exception {
        // Read MType
        byte mType = this.in.readByte();
        System.out.printf("mType %d >>>>> %c\n", (int) mType, (char) mType);
        switch (mType) {
            case (int) 'v':
                System.out.println("M Type was found of type v");
                decodeServerHandshake();
                break;
            case (int) 'E':
                System.out.println("M Type was found of type E");
                decodeErrorResponse();
                break;

            case (int) 'R':
                System.out.println("M Type was found of type R");
                decodeAuthenticationOK();
                break;

            case (int) 'K':
                System.out.println("M Type was found of type R");
                decodeServerKeyData();
                break;
            default:
                throw new Exception("MType was not recognized");
        }
        return "";
    }

    public void decodeServerKeyData() throws IOException {
        ServerKeyData serverKeyData = new ServerKeyData();
        serverKeyData.setMessageLength(in.readInt());
        //serverKeyData.setData(in.readByte());
        System.out.println(serverKeyData);
    }

    public void decodeAuthenticationOK() throws IOException {
        AuthenticationOK authSASL = new AuthenticationOK();
        authSASL.setMessageLength(in.readInt());
        authSASL.setAuthStatus(in.readInt());
        System.out.println(authSASL.toString());
    }

    public void decodeErrorResponse() throws IOException {
        System.out.println("Decoding the Error Response");
        ErrorResponse errorResponse = new ErrorResponse();
        //errorResponse.setmType((byte) 'E');
        errorResponse.setMessageLength(in.readInt());
        //errorResponse.setSeverity(in.readByte());
        errorResponse.setErrorCode(in.readInt());
        int messageLength = in.readInt();
        byte[] message = new byte[messageLength];
        in.read(message, 0, messageLength);
        errorResponse.setMessage(new String(message));
        errorResponse.setHeaderAttributeLength(in.readShort());

        System.out.println(errorResponse.toString());
    }

    public void decodeServerHandshake() throws IOException {
        ServerHandshake serverHandshake = new ServerHandshake();
        serverHandshake.setMessageLength(in.readInt());
        serverHandshake.setMajorVersion(in.readShort());
        serverHandshake.setMinorVersion(in.readShort());
        serverHandshake.setProtocolExtensionLength(in.readShort());
        System.out.println(serverHandshake);
    }

    public void decodeErrorResponseX() throws IOException {
//        System.out.println("Decoding the Error Response");
//        ErrorResponse errorResponse = new ErrorResponse();
//        Read read = new Reader(in);
//
//        errorResponse.setMType(read.readUint8());
//        errorResponse.setMessageLength(read.readUint32());
//        errorResponse.setSeverity(read.readUint8());
//        errorResponse.setErrorCode(read.readUint32());
//        errorResponse.setMessage(read.readString());
//        errorResponse.setHeaderAttributeLength(read.readUint16());
//
//        System.out.println(errorResponse.toString());
    }
}
