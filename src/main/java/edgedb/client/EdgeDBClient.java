package edgedb.client;

import edgedb.exceptions.*;
import edgedb.pipes.connect.ConnectionPipe;
import edgedb.pipes.executescript.ExecuteScriptPipe;
import edgedb.pipes.granularflow.GranularFlowPipe;
import edgedb.pipes.terminate.TerminatePipe;
import edgedb.protocol.client.Terminate;
import edgedb.protocol.server.PrepareComplete;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Data
@Slf4j
@NoArgsConstructor
public class EdgeDBClient {
    Connection connection;
    private SocketStream socketStream;

    public EdgeDBClient withDSN(String dsn) {
        log.debug("Trying to connect with client {}", dsn);
        connection = new Connection(dsn);
        log.debug("Connection {}", connection);
        return this;
    }


    public EdgeDBClient connect() throws FailedToConnectEdgeDBServer {
        log.debug("Connection to EdgeDB started with connection {}", connection);
        ConnectionPipe connectionPipe = new ConnectionPipe(connection);
        socketStream = connectionPipe.open();
        log.debug("Connection to EdgeDB was successful");
        return this;
    }

    public String executeScript(String script) throws EdgeDBServerException, FailedToDecodeServerResponseException, IOException, InterruptedException {
        log.debug("Started executing script {}", script);
        ExecuteScriptPipe executeScriptPipe = new ExecuteScriptPipe(socketStream);
        executeScriptPipe.write(script);
        Thread.sleep(10000);
        String result = executeScriptPipe.read();
        log.debug("Result from the Execute Script {}", result);
        return "";
    }

    public String execute(String query) throws IOException, FailedToDecodeServerResponseException {
        log.debug("Started executing statement {}",query);
        GranularFlowPipe granularFlowPipe = new GranularFlowPipe(socketStream);
        granularFlowPipe.setup(query);
        String result = granularFlowPipe.execute();
        return result;
    }

    //TODO: Should this return boolean flag
    public void terminate() throws IOException {
        log.debug("Trying to terminate EdgeDB connection");
        TerminatePipe terminatePipe = new TerminatePipe(socketStream);
        terminatePipe.terminate();
    }


}
