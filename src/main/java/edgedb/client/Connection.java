package edgedb.client;

import edgedb.exceptions.FailedToConnectEdgeDBServer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Data
@Slf4j
public class Connection {
    private String dsn;
    private String host;
    private int port;
    private String admin;
    private String user;
    private String password;
    private String database;
    private int timeout;


    /*dsn=None, *, host=None, port=None, admin=None, user=None, password=None, database=None, timeout=60*/

    public Connection(String dsn) {
        URI uri = URI.create(dsn);
        user = uri.getUserInfo();
        //database = uri.getPath();
        database = "tutorial";
        host = uri.getHost();
        port = uri.getPort();
    }

    public void connect() throws FailedToConnectEdgeDBServer {

//            Socket socket = new Socket();
//            socket.connect(new InetSocketAddress(host, port));
//            DataOutputStream dataOutputStream = new DataOutputStream(
//                    new BufferedOutputStream(socket.getOutputStream()));
//
//            DataInputStream dataInputStream = new DataInputStream(
//                    new BufferedInputStream(socket.getInputStream()));
//
//            writeClientHandshake(dataOutputStream);
//
//            // TODO: This code is ugly. Will need to refactor later big time.
//            ReaderFactory readerFactory = new ReaderFactory(dataInputStream);
//            Read reader = readerFactory.getReader();
//            BaseServerProtocol readProtocol = reader.read();
//
//        } catch (IOException ex) {
//            log.debug("Failed to connect to EdgeDB server {}", ex);
//            throw new FailedToConnectEdgeDBServer();
//        } catch (FailedToDecodeServerResponseException e) {
//            log.debug("Failed to read response from EdgeDB server {}", e);
//            throw new FailedToConnectEdgeDBServer();
//        }
    }
}
