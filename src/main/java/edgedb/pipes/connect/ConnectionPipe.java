package edgedb.pipes.connect;

import edgedb.client.Connection;
import edgedb.client.SocketStream;
import edgedb.exceptions.*;
import edgedb.pipes.BasePipe;
import edgedb.protocol.client.ClientHandshake;
import edgedb.protocol.client.SyncMessage;
import edgedb.protocol.client.writer.ClientHandshakeWriter;
import edgedb.protocol.client.writer.SyncMessageWriter;
import edgedb.protocol.server.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static edgedb.exceptions.ErrorMessage.DRIVER_INCOMPATIBLE_ERROR;
import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_TRANSACTION_STATE;
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
            setSocketStream(socket);

            writeAndFlush(new ClientHandshakeWriter(socketStream.getDataOutputStream(), new ClientHandshake(connection)));
            tryConnect();
            log.debug("After WriteHandshake");

            return socketStream;
        } catch (IOException | EdgeDBInternalErrException | EdgeDBSocketException e) {
            log.debug("Failed to connect to EdgeDB server {}", e);
            throw new EdgeDBFailedToConnectServer(e);
        }
    }

    public void setSocketStream(Socket socket) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(
                new BufferedOutputStream(socket.getOutputStream()));
        DataInputStream dataInputStream = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));

        this.setSocketStream(new SocketStream(dataInputStream, dataOutputStream));
    }

    public Socket openSocket() throws EdgeDBSocketException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(connection.getHost(), connection.getPort()));
            return socket;
        } catch (IOException e) {
            throw new EdgeDBSocketException(e, connection.getHost(), connection.getPort());
        }
    }

    public <T extends BaseServerProtocol> void tryConnect() throws IOException {
        while (socketStream.getDataInputStream().available() > 0) {
            T response = readServerResponse();
            if (response instanceof ServerHandshake) {
                ServerHandshake serverHandshake = (ServerHandshake) response;
                log.debug("Response is an Instance Of ServerHandshake {}", serverHandshake);
                throw new EdgeDBIncompatibleDriverException(DRIVER_INCOMPATIBLE_ERROR,
                        serverHandshake.getMajorVersion(),
                        serverHandshake.getMinorVersion());
            }

            if (response instanceof ReadyForCommand) {
                ReadyForCommand readyForCommand = (ReadyForCommand) response;
                log.debug("Response is an Instance Of ReadyForCommand {}", readyForCommand);

                switch (readyForCommand.getTransactionState()) {
                    case (int) IN_FAILED_TRANSACTION:
                        writeAndFlush(new SyncMessageWriter(socketStream.getDataOutputStream(), new SyncMessage()));
                        break;
                    case (int) IN_TRANSACTION:
                        break;
                    case (int) NOT_IN_TRANSACTION:
                        return;
                    default:
                        throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_TRANSACTION_STATE);
                }
            }

            if (response instanceof ServerAuthentication) {
                // TODO check if other type of authentication than OK.
            }

        }
    }

}
