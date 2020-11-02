package edgedb.pipes.executescript;

import edgedb.client.SocketStream;
import edgedb.exceptions.EdgeDBServerException;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.pipes.BasePipe;
import edgedb.pipes.pipe;
import edgedb.protocol.client.ExecuteScript;
import edgedb.protocol.client.writer.ExecuteScriptWriter;
import edgedb.protocol.server.CommandComplete;
import edgedb.protocol.server.ServerKeyData;
import edgedb.protocol.server.reader.CommandCompleteReader;
import edgedb.protocol.server.reader.ServerKeyDataReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_RESPONSE;
import static edgedb.protocol.constants.MessageType.*;

@AllArgsConstructor
@Slf4j
public class ExecuteScriptPipe implements pipe {
    SocketStream socketStream;

    public void write(String script) throws IOException {
        ExecuteScript executeScript = buildExecuteScript(script);
        writeExecuteScript(executeScript);
    }

    public String read() throws EdgeDBServerException, IOException, EdgeDBInternalErrException {
        CommandComplete commandComplete = readServerResponse(socketStream.getDataInputStream());
        log.debug("~~~~~~~~~~~~Printing Command Complete~~~~~~~~~~~~~~~~~");
        log.debug("Printing Command complete {}", commandComplete.toString());
        return commandComplete.getStatus();
    }

    public ExecuteScript buildExecuteScript(String script) {
        ExecuteScript executeScript = new ExecuteScript();
        executeScript.setHeadersLength((short) 0);
        executeScript.setScript(script);
        executeScript.setMessageLength(executeScript.calculateMessageLength());
        return executeScript;
    }

    public void writeExecuteScript(ExecuteScript executeScript) throws IOException {
        log.debug("Trying to write execute script {}", executeScript);
        ExecuteScriptWriter executeScriptWriter = new ExecuteScriptWriter(executeScript, socketStream.getDataOutputStream());
        executeScriptWriter.write();
    }

    public CommandComplete readServerResponse(DataInputStream dataInputStream) throws IOException, EdgeDBInternalErrException, EdgeDBServerException {
        byte mType = dataInputStream.readByte();
        log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);

        switch (mType) {
            case (int) COMMAND_COMPLETE:
                return readCommandComplete(dataInputStream);
            case (int) ERROR_RESPONSE:
                // We need to save the state here.
                throw new EdgeDBServerException();
            case (int) SERVER_KEY_DATA:
                ServerKeyData serverKeyData = readServerKeyData(dataInputStream);
                log.debug("Printing Server Key Data {}",serverKeyData);
            default:
                throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
        }
    }

    public CommandComplete readCommandComplete(DataInputStream dataInputStream) throws IOException {
        CommandCompleteReader reader = new CommandCompleteReader(dataInputStream);
        return reader.read();
    }

    public ServerKeyData readServerKeyData(DataInputStream dataInputStream) throws IOException {
        ServerKeyDataReader reader = new ServerKeyDataReader(dataInputStream);
        return reader.read();
    }
}

