package edgedb.client;

import edgedb.protocol.client.ExecuteScript;
import edgedb.protocol.server.reader.BaseDecoder;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EdgeDBClientX {

    private static String HARDCODED_SOCKET_ADDRESS = "10.199.198.56";

    //private static String HARDCODED_SOCKET_ADDRESS = "localhost";

    //public static int HARDCODED_SOCKET_PORT = 8083;
    public static int HARDCODED_SOCKET_PORT = 5656;
    private static int BUFFER_INC_SIZE = 4096;
    private static String ENCODING = "UTF-8";
    private final static int BYTEMASK = 0xFF; // 8 bits

    //final static Logger logger = LoggerFactory.getLogger(EdgeDBClient.class);
    public static void main(String[] args) throws IOException {

        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(HARDCODED_SOCKET_ADDRESS, HARDCODED_SOCKET_PORT));

        BufferedOutputStream bufOut = new BufferedOutputStream(socket.getOutputStream(), BUFFER_INC_SIZE);
        DataOutputStream out = new DataOutputStream(bufOut);
        //System.out.println("Before writing on output stream");

        //DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream(),BUFFER_INC_SIZE));
        InputStream in = socket.getInputStream();
        out.flush();
        //DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out.writeByte((int) 'V');// first 8 bits 0
        System.out.println("Wrote the messageType size {}" + out.size());
        out.writeInt(54); // message 32 bits message content 4
        System.out.println("Wrote the messageLength size {}" + out.size());
        out.writeShort(0); // short 16 bit major ver 6
        System.out.println("Wrote the MajorVersion size {}" + out.size());

        out.writeShort(8); //short 16 bit minor ver 8
        System.out.println("Wrote the MinorVersion size {}" + out.size());

        out.writeShort(2); // 10
        byte[] tillTheEnd = new byte[BUFFER_INC_SIZE - out.size()];

        System.out.println("Wrote the Connection Params number size{}" + out.size());


        //a UTF-8 encoded text string prefixed with its byte length as uint32

        String[] connectionParams = {
                "user", "edgedb",
                "database", "tutorial",
        };

        for (String param : connectionParams) {
            int paramByteArrayLength = param.getBytes().length;
            out.writeInt(paramByteArrayLength);
            out.write(param.getBytes(), 0, param.getBytes().length);
            System.out.println("Wrote the Connection Params {} sizeNow {}" + param + out.size());
        }

        out.writeShort(0);
        System.out.println("Wrote the ProtocolExtension size{}" + out.size());

        //System.out.println("before Flush");
        System.out.println("Before Flush");

//        byte[] remainingBytes = new byte[BUFFER_INC_SIZE-out.size()];
//        out.write(remainingBytes,0,remainingBytes.length);
        out.flush();

        BufferedInputStream bInput = new BufferedInputStream(in);
        DataInputStream dInput = new DataInputStream(bInput);
        BaseDecoder decoder = new BaseDecoder(dInput);

        System.out.println("printing Before Decodign Client");
        try {
            decoder.decode();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Send the Execute.
        ExecuteScript executeScript = new ExecuteScript();
        executeScript.setHeadersLength((short) 0);
        executeScript.setScript("SELECT Movie { id, title, year }");
        try {
            encodeExecuteScript(executeScript, out);
            decoder.decode();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        socket.close();
    }

    public static void encodeExecuteScript(ExecuteScript executeScript, DataOutputStream out) throws IOException {
        out.flush();
        out.writeByte((int) 'Q');
        String script = executeScript.getScript();
        int scriptLength = script.getBytes().length;
        out.writeInt(Integer.SIZE / Byte.SIZE +
                Short.SIZE / Byte.SIZE +
                scriptLength);
        out.writeShort(executeScript.getHeadersLength());
        out.writeInt(scriptLength);
        out.write(script.getBytes(), 0, scriptLength);
        out.flush();
    }

//    public void decodeErrorResponse() throws IOException {
//        System.out.println("Decoding the Error Response");
//        ErrorResponse errorResponse = new ErrorResponse();
//        //Read read = new Reader(in);
//
//        errorResponse.setMType();
//        errorResponse.setMessageLength(read.readUint32());
//        errorResponse.setSeverity(read.readUint8());
//        errorResponse.setErrorCode(read.readUint32());
//        errorResponse.setMessage(read.readString());
//        errorResponse.setHeaderAttributeLength(read.readUint16());
//
//        System.out.println(errorResponse.toString());
//    }

    public static String byteArrayToDecimalString(byte[] bArray) {
        StringBuilder rtn = new StringBuilder();
        for (byte b : bArray) {
            rtn.append(b & BYTEMASK).append(" ");
        }
        return rtn.toString();
    }

    public static final byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }

}
