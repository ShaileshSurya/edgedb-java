package edgedb.internal.pipes.connect;

import edgedb.client.*;
import edgedb.exceptions.*;
import edgedb.internal.pipes.BasePipe;
import edgedb.internal.protocol.client.ClientHandshake;
import edgedb.internal.protocol.client.SyncMessage;
import edgedb.internal.protocol.client.writer.ClientHandshakeWriter;
import edgedb.internal.protocol.client.writer.SyncMessageWriter;
import edgedb.internal.protocol.server.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import static edgedb.exceptions.ErrorMessage.*;
import static edgedb.internal.protocol.constants.TransactionState.*;


@Slf4j
public class ConnectionPipe extends BasePipe {

    private Connection connection;

    public ConnectionPipe(Connection connection) {
        super(null);
        this.connection = connection;
    }

    public SocketStream open() throws Throwable {
        try {
            log.debug("Opening up a socket");
            Socket socket = openSocket();
            setSocketStream(socket);

            writeAndFlush(new ClientHandshakeWriter(socketStream.getDataOutputStream(), new ClientHandshake(connection)));
            tryConnect();
            log.debug("Connection Successful");
            return socketStream;
        } catch (IOException | EdgeDBInternalErrException | EdgeDBSocketException | EdgeDBIncompatibleDriverException e) {
            log.debug("Failed to connect to EdgeDB server {}", e);
            throw new EdgeDBFailedToConnectServer(FAILED_TO_CONNECT_EDGEDB_SERVER).initCause(e);
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

    public <T extends BaseServerProtocol> void tryConnect() throws IOException, EdgeDBInternalErrException, EdgeDBIncompatibleDriverException {
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
