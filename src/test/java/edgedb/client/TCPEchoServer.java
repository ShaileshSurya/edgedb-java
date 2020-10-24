package edgedb.client;

import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPEchoServer {

    private static final int BUFSIZE = 32;   // Size of receive buffer

    public static void main(String[] args) throws IOException {

        args = new String[]{"8083"};
        if (args.length != 1)  // Test for correct # of args
        {
            throw new IllegalArgumentException("Parameter(s): <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);

        int recvMsgSize;   // Size of received message
        byte[] byteBuffer = new byte[BUFSIZE];  // Receive buffer

        for (; ; ) { // Run forever, accepting and servicing connections
            Socket clntSock = servSock.accept();     // Get client connection

            System.out.println("Handling client at " +
                    clntSock.getInetAddress().getHostAddress() + " on port " +
                    clntSock.getPort());

            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();

            // Receive until client closes connection, indicated by -1 return
            while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                BigInteger one = new BigInteger(byteBuffer);
                // Decimal
//                System.out.println(one.toString());
//                for(byte b:byteBuffer){
//                    System.out.println();
//                }
                System.out.println(byteArrayToDecimalString(byteBuffer));
                out.write(byteBuffer, 0, recvMsgSize);
            }


            clntSock.close();  // Close the socket.  We are done with this client!
        }
        /* NOT REACHED */
    }

    private final static int BYTEMASK = 0xFF; // 8 bits

    public static String byteArrayToDecimalString(byte[] bArray) {
        StringBuilder rtn = new StringBuilder();
        for (byte b : bArray) {
            rtn.append(b & BYTEMASK).append(" ");
        }
        return rtn.toString();
    }
}
