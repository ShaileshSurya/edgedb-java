package edgedb.client;

import edgedb.exceptions.FailedToConnectEdgeDBServer;
import edgedb.exceptions.FailedToDecodeServerResponseException;
import edgedb.pipes.connect.ConnectionPipe;
import edgedb.protocol.client.ClientHandshake;
import edgedb.protocol.client.writer.ClientHandshakeWriter;
import edgedb.protocol.client.writer.Write;
import edgedb.protocol.server.BaseServerProtocol;
import edgedb.protocol.server.reader.Read;
import edgedb.protocol.server.reader.ReaderFactory;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.*;

@Data
@Slf4j
public class Connection {
    @NonNull String user;
    @NonNull String database;
    @NonNull String address;
    @NonNull int port;

    public Connection(String dsn) {
        URI uri = URI.create(dsn);
        user = uri.getUserInfo();
        database = uri.getPath();
        address = uri.getHost();
        port = uri.getPort();
    }

    public void connect() throws FailedToConnectEdgeDBServer {
        try {

            ConnectionPipe connectionPipe = new ConnectionPipe();
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(address, port));
            DataOutputStream dataOutputStream = new DataOutputStream(
                    new BufferedOutputStream(socket.getOutputStream()));

            DataInputStream dataInputStream = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            writeClientHandshake(dataOutputStream);

            // TODO: This code is ugly. Will need to refactor later big time.
            ReaderFactory readerFactory = new ReaderFactory(dataInputStream);
            Read reader = readerFactory.getReader();
            BaseServerProtocol readProtocol = reader.read();

        } catch (IOException ex) {
            log.debug("Failed to connect to EdgeDB server {}", ex);
            throw new FailedToConnectEdgeDBServer();
        } catch (FailedToDecodeServerResponseException e) {
            log.debug("Failed to read response from EdgeDB server {}", e);
            throw new FailedToConnectEdgeDBServer();
        }
    }


    private void writeClientHandshake(DataOutputStream dataOutputStream) throws IOException {
        ClientHandshake clientHandshake = new ClientHandshake(this);
        Write write = new ClientHandshakeWriter(clientHandshake, dataOutputStream);
        write.encode();
    }
}
