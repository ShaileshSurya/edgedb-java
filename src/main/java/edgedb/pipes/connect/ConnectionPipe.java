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
import edgedb.protocol.server.reader.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static edgedb.protocol.constants.MessageType.*;

import static edgedb.protocol.constants.TransactionState.*;


@Slf4j
public class ConnectionPipe implements pipe {

    private Connection connection;


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

            writeAndFlushClientHandshake(dataOutputStream);
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

    private void writeAndFlushClientHandshake(DataOutputStream dataOutputStream) throws IOException {
        log.debug("Trying to write client handshake");
        ClientHandshake clientHandshake = new ClientHandshake(connection);
        Write write = new ClientHandshakeWriter(dataOutputStream, clientHandshake);
        write.writeAndFlush();
        log.debug("Write client handshake successful");
    }

    private void readServerResponse(DataInputStream dataInputStream) throws IOException, FailedToDecodeServerResponseException {
        log.debug("Trying to read Server Response");


        while (true) {
            byte mType = dataInputStream.readByte();
            log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);
            switch (mType) {
                case (int) SERVER_HANDSHAKE:
                    log.debug("Server response SERVER_HANDSHAKE received");
                    ServerHandshake serverHandshake = readServerHandshake(dataInputStream);
                    log.debug("Server Handshake {}", serverHandshake);
                    throw new FailedToDecodeServerResponseException();
                case (int) SERVER_KEY_DATA:
                    ServerKeyData serverKeyData = readServerKeyData(dataInputStream);
                    log.debug("Printing Server Key Data {}", serverKeyData);
                    break;
                case (int) READY_FOR_COMMAND:
                    ReadyForCommandReader readyForCommandReader = new ReadyForCommandReader(dataInputStream);
                    ReadyForCommand readyForCommand = readyForCommandReader.read();
                    log.info("Ready For Command Reader {}", readyForCommand);
                    log.info("Transaction State {} {}",readyForCommand.getTransactionState(),decodeTransactionState(readyForCommand.getTransactionState()));
                    log.info("No of bytes available to read {}",dataInputStream.available());
                    return;
                case (int) SERVER_AUTHENTICATION:
                    log.debug("Server response AUTHENTICATION_OK received");
                    ServerAuthentication authentication = readAuthentication(dataInputStream);
                    log.debug("AuthenticationOk {}", authentication);
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
    }

    public String decodeTransactionState(short state){

        switch (state) {
            case (int) IN_TRANSACTION:
                return "IN_TRANSACTION";
            case (int) IN_FAILED_TRANSACTION:
                return "IN_FAILED_TRANSACTION";
            case (int) NOT_IN_TRANSACTION:
                return "NOT_IN_TRANSACTION";
            default:
                return "";
        }
    }

    public ServerKeyData readServerKeyData(DataInputStream dataInputStream) throws IOException {
        ServerKeyDataReader reader = new ServerKeyDataReader(dataInputStream);
        return reader.read();
    }

    private void readErrorResponse(DataInputStream dataInputStream) {
        log.debug("Trying to read Error Response");
    }

    private ServerHandshake readServerHandshake(DataInputStream dataInputStream) throws IOException {
        log.debug("Trying to read Server Handshake");
        ServerHandshakeReader reader = new ServerHandshakeReader(dataInputStream);
        return reader.read();
    }

    private ServerAuthentication readAuthentication(DataInputStream dataInputStream) throws IOException, FailedToDecodeServerResponseException {
        log.debug("Trying to read Authentication OK");
        ServerAuthenticationReader reader = new ServerAuthenticationReader(dataInputStream);
        return reader.read();
    }
}
