package edgedb.pipes.connect;

import edgedb.client.Connection;
import edgedb.client.SocketStream;
import edgedb.exceptions.FailedToConnectEdgeDBServer;
import edgedb.exceptions.FailedToDecodeServerResponseException;
import edgedb.pipes.pipe;
import edgedb.protocol.client.ClientHandshake;
import edgedb.protocol.client.writer.ClientHandshakeWriter;
import edgedb.protocol.client.writer.Write;
import edgedb.protocol.server.*;
import edgedb.protocol.server.reader.AuthenticationOKReader;
import edgedb.protocol.server.reader.ServerHandshakeReader;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static edgedb.protocol.constants.MessageType.*;

@Slf4j
public class ConnectionPipe implements pipe {

    private Connection connection;
    private AuthenticationOK authenticationOK;
    private ServerHandshake serverHandshake;
    private ErrorResponse errorResponse;

    public ConnectionPipe(Connection connection) {
        this.connection = connection;
    }

    public SocketStream open() throws FailedToConnectEdgeDBServer {
        try {
            log.debug("Opening up a socket");
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(connection.getHost(), connection.getPort()));
            DataOutputStream dataOutputStream = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            DataInputStream dataInputStream = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            writeClientHandshake(dataOutputStream);
            readServerResponse(dataInputStream);
            log.debug("After WriteHandshake");
            /*try{
                while(true) {

                    byte ch;
                    ch = dataInputStream.readByte();
                    System.out.print((char) ch + "\t");
                }
            }catch (EOFException e){
                log.debug("EOF Exception {}", e);
            }*/

            return new SocketStream(dataInputStream, dataOutputStream);

        } catch (IOException e) {
            log.debug("Failed to connect to EdgeDB server {}", e);
            throw new FailedToConnectEdgeDBServer();

        } catch (FailedToDecodeServerResponseException e) {
            log.debug("Failed to connect to EdgeDB server {}", e);
            throw new FailedToConnectEdgeDBServer();
        }
    }

    private void writeClientHandshake(DataOutputStream dataOutputStream) throws IOException {
        log.debug("Trying to write client handshake");
        ClientHandshake clientHandshake = new ClientHandshake(connection);
        Write write = new ClientHandshakeWriter(dataOutputStream, clientHandshake);
        write.write();
        log.debug("Write client handshake successful");
    }

    private void readServerResponse(DataInputStream dataInputStream) throws IOException, FailedToDecodeServerResponseException {
        log.debug("Trying to read Server Response");
        byte mType = dataInputStream.readByte();

        log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);

        switch (mType) {
            case (int) SERVER_HANDSHAKE:
                log.debug("Server response SERVER_HANDSHAKE received");
                ServerHandshake serverHandshake = readServerHandshake(dataInputStream);
                log.debug("Server Handshake {}", serverHandshake);
                break;

            case (int) AUTHENTICATION_OK:
                log.debug("Server response AUTHENTICATION_OK received");
                AuthenticationOK authenticationOK= readAuthenticationOK(dataInputStream);
                log.debug("AuthenticationOk {}", authenticationOK);
                break;
            case (int) ERROR_RESPONSE:
                log.debug("Server response AUTHENTICATION_OK received");
                readErrorResponse(dataInputStream);
                throw new FailedToDecodeServerResponseException();
            default:
                log.debug("Failed to decode Server Response");
                throw new FailedToDecodeServerResponseException();
        }
    }

    private void readErrorResponse(DataInputStream dataInputStream) {
        log.debug("Trying to read Error Response");
    }

    private ServerHandshake readServerHandshake(DataInputStream dataInputStream) throws IOException {
        log.debug("Trying to read Server Handshake");
        ServerHandshakeReader reader = new ServerHandshakeReader(dataInputStream);
        return reader.read();
    }

    private AuthenticationOK readAuthenticationOK(DataInputStream dataInputStream) throws IOException {
        log.debug("Trying to read Authentication OK");
        AuthenticationOKReader reader = new AuthenticationOKReader(dataInputStream);
        return reader.read();
    }
}
