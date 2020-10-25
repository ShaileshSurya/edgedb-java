package edgedb.pipes.granularflow;

import edgedb.client.SocketStream;
import edgedb.exceptions.FailedToDecodeServerResponseException;
import edgedb.protocol.client.*;
import edgedb.protocol.client.writer.*;
import edgedb.protocol.constants.*;
import edgedb.protocol.server.*;
import edgedb.protocol.server.reader.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;

import static edgedb.protocol.constants.MessageType.*;

@Slf4j
@AllArgsConstructor
public class GranularFlowPipe {
    SocketStream socketStream;

    public void setup(String command) throws IOException, FailedToDecodeServerResponseException {
        writePrepareMessage(command);
        writeSyncMessageAndFlush();
        PrepareComplete prepareComplete = readPrepareCompleteServerResponse();

        log.debug("PrepareComplete {}", prepareComplete);
    }

    public PrepareComplete readPrepareCompleteServerResponse() throws IOException, FailedToDecodeServerResponseException {

        PrepareComplete prepareComplete = null;
        while (true) {
            byte mType = socketStream.getDataInputStream().readByte();
            log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);

            switch (mType) {
                case (int) SERVER_KEY_DATA:
                    ServerKeyData serverKeyData = readServerKeyData(socketStream.getDataInputStream());
                    log.debug("Printing Server Key Data {}", serverKeyData);
                    break;
                case (int) SERVER_AUTHENTICATION:
                    ServerAuthenticationReader serverAuthenticationReader = new ServerAuthenticationReader(socketStream.getDataInputStream());
                    ServerAuthentication serverAuthentication = serverAuthenticationReader.read();
                    break;
                case (int) PREPARE_COMPLETE:
                    PrepareCompleteReader prepareCompleteReader = new PrepareCompleteReader(socketStream.getDataInputStream());
                    prepareComplete = prepareCompleteReader.read();
                    log.debug("Printing Prepare Complete {}",prepareComplete);
                    break;
                case (int) READY_FOR_COMMAND:
                    ReadyForCommandReader readyForCommandReader = new ReadyForCommandReader(socketStream.getDataInputStream());
                    ReadyForCommand readyForCommand = readyForCommandReader.read();
                    log.info("Ready For Command Reader {}", readyForCommand);
                    return prepareComplete;
                default:
                    throw new FailedToDecodeServerResponseException();
            }
        }
    }

    public ServerKeyData readServerKeyData(DataInputStream dataInputStream) throws IOException {
        ServerKeyDataReader reader = new ServerKeyDataReader(dataInputStream);
        return reader.read();
    }

    private void writePrepareMessage(String command) throws IOException {
        Prepare prepare = buildPrepareMessage(command);
        PrepareWriter writer = new PrepareWriter(socketStream.getDataOutputStream(), prepare);
        writer.write();
    }

    public void writeExecuteMessage(String query) throws IOException {
        Execute execute = buildExecuteMessage();
        log.debug("Execute Script {}",execute);
        ExecuteWriter executeWriter = new ExecuteWriter(socketStream.getDataOutputStream(),execute);
        executeWriter.write();
    }

    public Execute buildExecuteMessage(){
        log.debug("Trying to build execute Script");
        Execute execute = new Execute();
        execute.setHeadersLength((short) 0);
        execute.setStatementName("".getBytes());
        execute.setArguments("".getBytes());
        execute.setMessageLength(18);
        return execute;
    }

    public Prepare buildPrepareMessage(String command) {
        Prepare prepare = new Prepare();
        prepare.setHeadersLength((short) 0);
        prepare.setIoFormat((byte) IOFormat.BINARY);
        prepare.setExpectedCardinality((byte) Cardinality.MANY);
        prepare.setStatementName("".getBytes());
        prepare.setCommand(command);
        prepare.setMessageLength(prepare.calculateMessageLength());
        return prepare;
    }

    private void writeSyncMessageAndFlush() throws IOException {
        log.debug("Trying to write Sync message");
        SyncMessage syncMessage = buildSyncMessage();
        log.debug("Sync Message {}",syncMessage);
        SyncMessageWriter syncMessageWriter = new SyncMessageWriter(socketStream.getDataOutputStream(),syncMessage);
        syncMessageWriter.writeAndFlush();
    }

    public SyncMessage buildSyncMessage(){
        log.debug("Trying to build Sync message");
        SyncMessage syncMessage = new SyncMessage();
        syncMessage.setMessageLength(syncMessage.calculateMessageLength());
        return syncMessage;
    }

    public DataResponse readExecuteServerResponse() throws IOException, FailedToDecodeServerResponseException {
        DataResponse dataResponse = null;
        while (true) {
            byte mType = socketStream.getDataInputStream().readByte();
            log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);

            switch (mType) {
                case (int) DATA_RESPONSE:
                    dataResponse = readDataResponse();
                    log.debug("Printing Server Key Data {}", dataResponse);
                    break;
                case (int) COMMAND_COMPLETE:
                    CommandComplete commandComplete = readCommandComplete();
                    log.debug("Printing Server Key Data {}", commandComplete);
                    break;
                default:
                    throw new FailedToDecodeServerResponseException();
            }
        }
    }

    public DataResponse readDataResponse() throws IOException {
        DataResponseReader reader = new DataResponseReader(socketStream.getDataInputStream());
        return reader.read();
    }


    public CommandComplete readCommandComplete() throws IOException {
        CommandCompleteReader reader = new CommandCompleteReader(socketStream.getDataInputStream());
        return reader.read();
    }
    public String execute(String query) throws IOException, FailedToDecodeServerResponseException {
        log.info("Trying to execute in granular flow");
        writeExecuteMessage(query);
        writeSyncMessageAndFlush();
        DataResponse response = readExecuteServerResponse();
        log.debug("Data Response {}",response);
        return "";
    }

}
