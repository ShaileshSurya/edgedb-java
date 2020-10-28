package edgedb.pipes.connect;

import edgedb.client.Connection;
import edgedb.client.SocketStream;
import edgedb.exceptions.FailedToConnectEdgeDBServer;
import edgedb.exceptions.FailedToDecodeServerResponseException;
import edgedb.pipes.pipe;
import edgedb.protocol.client.*;
import edgedb.protocol.client.writer.*;
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

            writeAndFlush(new ClientHandshakeWriter(dataOutputStream,new ClientHandshake(connection)));

            readServerResponse(dataInputStream,dataOutputStream);
            log.debug("After WriteHandshake");

            return new SocketStream(dataInputStream, dataOutputStream);

        } catch (IOException e) {
            log.debug("Failed to connect to EdgeDB server {}", e);
            throw new FailedToConnectEdgeDBServer();

        } catch (FailedToDecodeServerResponseException e) {
            log.debug("Failed to connect to EdgeDB server {}", e);
            throw new FailedToConnectEdgeDBServer();
        }
    }


    private void readServerResponse(DataInputStream dataInputStream,DataOutputStream dataOutputStream) throws IOException, FailedToDecodeServerResponseException {
        log.debug("Trying to read Server Response");

        while (true) {
            byte mType = dataInputStream.readByte();
            log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);
            switch (mType) {
                case (int) SERVER_HANDSHAKE:
                    log.debug("Server response SERVER_HANDSHAKE received");
                    ServerHandshake serverHandshake = read(new ServerHandshakeReader(dataInputStream));
                    log.debug("Server Handshake {}", serverHandshake);
                    throw new FailedToDecodeServerResponseException();
                case (int) SERVER_KEY_DATA:
                    ServerKeyData serverKeyData = read(new ServerKeyDataReader(dataInputStream));
                    log.debug("Printing Server Key Data {}", serverKeyData);
                    break;
                case (int) READY_FOR_COMMAND:
                    ReadyForCommand readyForCommand = read(new ReadyForCommandReader(dataInputStream));
                    log.info("Ready For Command Reader {}", readyForCommand);

                    switch (readyForCommand.getTransactionState()){
                        case (int)IN_FAILED_TRANSACTION:
                            writeAndFlush(new SyncMessageWriter(dataOutputStream,buildSyncMessage()));
                            break;
                        case (int)IN_TRANSACTION:
                            break;
                        case (int)NOT_IN_TRANSACTION:
                            return;
                        default:
                            throw new FailedToDecodeServerResponseException();
                    }
                case (int) SERVER_AUTHENTICATION:
                    log.debug("Server response AUTHENTICATION_OK received");
                    ServerAuthentication authentication = read(new ServerAuthenticationReader(dataInputStream));
                    log.debug("AuthenticationOk {}", authentication);
                    break;
                case (int) ERROR_RESPONSE:
                    log.debug("Server response AUTHENTICATION_OK received");
                    ErrorResponse errorResponse = read(new ErrorResponseReader(dataInputStream));
                    throw new FailedToDecodeServerResponseException();
                default:
                    log.debug("Failed to decode Server Response");
                    throw new FailedToDecodeServerResponseException();
            }
        }
    }

    public <S extends Read,T extends BaseServerProtocol> T read(S reader) throws IOException, FailedToDecodeServerResponseException {
        return (T) reader.read();
    }

    public <S extends BaseWriter> void write(S writer) throws IOException {
        writer.write();
    }

    public <S extends BaseWriter> void writeAndFlush(S writer) throws IOException {
        writer.writeAndFlush();
    }

    public SyncMessage buildSyncMessage(){
        log.debug("Trying to build Sync message");
        SyncMessage syncMessage = new SyncMessage();
        syncMessage.setMessageLength(syncMessage.calculateMessageLength());
        return syncMessage;
    }
}
