package edgedb.pipes.connect;

import edgedb.client.Connection;
import edgedb.client.SocketStream;
import edgedb.exceptions.*;
import edgedb.pipes.BasePipe;
import edgedb.protocol.client.*;
import edgedb.protocol.client.writer.*;
import edgedb.protocol.server.*;
import edgedb.protocol.server.reader.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_RESPONSE;
import static edgedb.protocol.constants.MessageType.*;

import static edgedb.protocol.constants.TransactionState.*;


@Slf4j
public class ConnectionPipe extends BasePipe {

    private Connection connection;

    public ConnectionPipe(Connection connection) {
        super(null);
        this.connection = connection;
    }

    public SocketStream open() {
        try {
            log.debug("Opening up a socket");
            Socket socket = openSocket();

            DataOutputStream dataOutputStream = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            DataInputStream dataInputStream = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            writeAndFlush(new ClientHandshakeWriter(dataOutputStream,new ClientHandshake(connection)));
            readServerResponse(dataInputStream,dataOutputStream);
            log.debug("After WriteHandshake");

            return new SocketStream(dataInputStream, dataOutputStream);

        } catch (IOException | EdgeDBInternalErrException | EdgeDBSocketException e) {
            log.debug("Failed to connect to EdgeDB server {}", e);
            throw new EdgeDBFailedToConnectServer(e);
        }
    }

    public Socket openSocket() throws EdgeDBSocketException{
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(connection.getHost(), connection.getPort()));
            return socket;
        } catch (IOException e) {
            throw new EdgeDBSocketException(e,connection.getHost(),connection.getPort());
        }
    }

    private void readServerResponse(DataInputStream dataInputStream,DataOutputStream dataOutputStream) throws IOException, EdgeDBInternalErrException {
        log.debug("Trying to read Server Response");

        while (true) {
            byte mType = dataInputStream.readByte();
            log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);
            switch (mType) {
                case (int) SERVER_HANDSHAKE:
                    log.debug("Server response SERVER_HANDSHAKE received");
                    ServerHandshake serverHandshake = read(new ServerHandshakeReader(dataInputStream));
                    log.debug("Server Handshake {}", serverHandshake);
                    throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
                case (int) SERVER_KEY_DATA:
                    ServerKeyData serverKeyData = read(new ServerKeyDataReader(dataInputStream));
                    log.debug("Printing Server Key Data {}", serverKeyData);
                    break;
                case (int) READY_FOR_COMMAND:
                    ReadyForCommand readyForCommand = read(new ReadyForCommandReader(dataInputStream));
                    log.info("Ready For Command Reader {}", readyForCommand);

                    switch (readyForCommand.getTransactionState()){
                        case (int)IN_FAILED_TRANSACTION:
                            writeAndFlush(new SyncMessageWriter(dataOutputStream,new SyncMessage()));
                            break;
                        case (int)IN_TRANSACTION:
                            break;
                        case (int)NOT_IN_TRANSACTION:
                            return;
                        default:
                            throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
                    }
                case (int) SERVER_AUTHENTICATION:
                    log.debug("Server response AUTHENTICATION_OK received");
                    ServerAuthentication authentication = read(new ServerAuthenticationReader(dataInputStream));
                    log.debug("AuthenticationOk {}", authentication);
                    break;
                case (int) ERROR_RESPONSE:
                    log.debug("Server response AUTHENTICATION_OK received");
                    ErrorResponse errorResponse = read(new ErrorResponseReader(dataInputStream));
                    throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
                default:
                    log.debug("Failed to decode Server Response");
                    throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
            }
        }
    }
}
