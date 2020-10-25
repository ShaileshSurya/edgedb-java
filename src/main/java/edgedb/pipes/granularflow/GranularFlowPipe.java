package edgedb.pipes.granularflow;

import edgedb.client.SocketStream;
import edgedb.exceptions.FailedToDecodeServerResponseException;
import edgedb.protocol.client.*;
import edgedb.protocol.client.writer.PrepareWriter;
import edgedb.protocol.client.writer.SyncMessageWriter;
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

        log.info("readPrepareCompleteServerResponse~~~~~");
        int iter =0;

        while(socketStream.getDataInputStream().available() < 1 && iter <10){
            try {
                log.debug("In Iterator");
                log.debug("~~readPrepareCompleteServerResponse~~~~~");
                iter++ ;
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new FailedToDecodeServerResponseException();
            }
        }

        if (iter == 10){
            log.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
                    "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" +
                    "~~~~~~~~~~~~~~~~~~~ERORE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            throw new FailedToDecodeServerResponseException();
        }

        log.info("No of bytes available to read {}",socketStream.getDataInputStream().available());
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
                    PrepareComplete prepareComplete = prepareCompleteReader.read();
                    return prepareComplete;
                case (int) READY_FOR_COMMAND:
                    ReadyForCommandReader readyForCommandReader = new ReadyForCommandReader(socketStream.getDataInputStream());
                    ReadyForCommand readyForCommand = readyForCommandReader.read();
                    log.info("Ready For Command Reader {}", readyForCommand);
                    break;
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
        SyncMessage syncMessage = buildSyncMessage();
        SyncMessageWriter syncMessageWriter = new SyncMessageWriter(socketStream.getDataOutputStream(),syncMessage);
        syncMessageWriter.writeAndFlush();
    }

    public SyncMessage buildSyncMessage(){
        SyncMessage syncMessage = new SyncMessage();
        syncMessage.setMessageLength(syncMessage.calculateMessageLength());
        return syncMessage;
    }
    public void write() {

    }

}
