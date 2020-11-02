package edgedb.pipes.granularflow;

import edgedb.client.SocketStream;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.pipes.BasePipe;
import edgedb.protocol.client.*;
import edgedb.protocol.client.writer.*;
import edgedb.protocol.constants.*;
import edgedb.protocol.server.*;
import edgedb.protocol.server.reader.*;
import edgedb.protocol.typedescriptor.BaseScalarType;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_RESPONSE;
import static edgedb.protocol.constants.MessageType.*;

@Slf4j
public class GranularFlowPipe extends BasePipe {
    BaseScalarType argumentType;
    BaseScalarType resultType;

    public GranularFlowPipe(SocketStream socketStream){
        super(socketStream);
    }

    public void setup(String command) throws IOException, EdgeDBInternalErrException {
        write(new PrepareWriter(socketStream.getDataOutputStream(),buildPrepareMessage(command)));
        writeAndFlush(new SyncMessageWriter(socketStream.getDataOutputStream(),buildSyncMessage()));

        PrepareComplete prepareComplete = readPrepareCompleteServerResponse();
        argumentType= prepareComplete.getArgumentDataDiscriptor();
        resultType= prepareComplete.getResultDataDescriptor();
    }

    public PrepareComplete readPrepareCompleteServerResponse() throws IOException, EdgeDBInternalErrException {

        PrepareComplete prepareComplete = null;
        while (true) {
            byte mType = socketStream.getDataInputStream().readByte();
            log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);

            switch (mType) {
                case (int) SERVER_KEY_DATA:
                    ServerKeyData serverKeyData = read(new ServerKeyDataReader(socketStream.getDataInputStream()));
                    log.debug("Printing Server Key Data {}", serverKeyData);
                    break;
                case (int) SERVER_AUTHENTICATION:
                    ServerAuthentication serverAuthentication = read(new ServerAuthenticationReader(socketStream.getDataInputStream()));
                    break;
                case (int) PREPARE_COMPLETE:
                    prepareComplete = read(new PrepareCompleteReader(socketStream.getDataInputStream()));
                    log.debug("Printing Prepare Complete {}",prepareComplete);
                    break;
                case (int) READY_FOR_COMMAND:
                    ReadyForCommand readyForCommand =read(new ReadyForCommandReader(socketStream.getDataInputStream()));
                    log.info("Ready For Command Reader {}", readyForCommand);
                    if(prepareComplete==null){
                        writeAndFlush(new SyncMessageWriter(socketStream.getDataOutputStream(),buildSyncMessage()));
                        break;
                    }
                    return prepareComplete;
                default:
                    throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
            }
        }
    }

    public Execute buildExecuteMessage(){
        return new Execute();
    }

    public Prepare buildPrepareMessage(String command) {
        return new Prepare(IOFormat.JSON,Cardinality.MANY,command);
    }


    public SyncMessage buildSyncMessage(){
        return new SyncMessage();
    }


    public String execute() throws IOException, EdgeDBInternalErrException {
        log.info("Trying to execute in granular flow");
        write(new ExecuteWriter(socketStream.getDataOutputStream(),buildExecuteMessage()));
        writeAndFlush(new SyncMessageWriter(socketStream.getDataOutputStream(),buildSyncMessage()));

        DataResponse response = readExecuteServerResponse();
        byte[] responseByteArray = response.getDataElements()[0].getDataElement();
        return new String(responseByteArray);
    }

    public DataResponse readExecuteServerResponse() throws IOException, EdgeDBInternalErrException {
        DataResponse dataResponse = null;
        while (true) {
            byte mType = socketStream.getDataInputStream().readByte();
            log.debug("MType was found of Decimal Value {} and Char Value {}", (int) mType, (char) mType);

            switch (mType) {
                case (int) DATA_RESPONSE:
                    log.debug("Trying to read DataResponse");
                    dataResponse = read(new DataResponseReader(socketStream.getDataInputStream()));
                    log.debug("Printing Data Response {}", dataResponse);
                    break;
                case (int) COMMAND_COMPLETE:
                    CommandComplete commandComplete = read(new CommandCompleteReader(socketStream.getDataInputStream()));
                    log.debug("Printing Server Key Data {}", commandComplete);
                    return dataResponse;
                default:
                    throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_RESPONSE);
            }
        }
    }

}
