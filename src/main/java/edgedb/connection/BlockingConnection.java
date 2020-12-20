package edgedb.connection;

import edgedb.client.*;
import edgedb.exceptions.*;
import edgedb.internal.pipes.SyncFlow.SyncPipe;
import edgedb.internal.pipes.SyncFlow.SyncPipeImpl;
import edgedb.internal.pipes.granularflow.GranularFlowPipeV2;
import edgedb.internal.pipes.granularflow.IGranularFlowPipe;
import edgedb.internal.protocol.client.*;
import edgedb.internal.protocol.client.writerV2.ChannelProtocolWriterImpl;
import edgedb.internal.protocol.constants.Cardinality;
import edgedb.internal.protocol.constants.IOFormat;
import edgedb.internal.protocol.server.*;
import edgedb.internal.protocol.server.readerv2.*;
import edgedb.internal.protocol.typedescriptor.TypeDescriptor;
import edgedb.internal.protocol.typedescriptor.decoder.TypeDecoderFactoryImpl;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.exceptions.ErrorMessage.FAILED_TO_DECODE_SERVER_TRANSACTION_STATE;
import static edgedb.internal.protocol.constants.CommonConstants.BUFFER_SIZE;
import static edgedb.internal.protocol.constants.TransactionState.*;

@Slf4j
public class BlockingConnection extends BaseConnection {
    ByteBuffer buffer;

    @Override
    protected boolean isBlocking() {
        return true;
    }

    @Override
    protected IConnection getConnection() {
        return this;
    }

    @Override
    public ResultSet query(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.BINARY, Cardinality.MANY, query);
    }

    @Override
    public ResultSet queryOne(String command) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.BINARY, Cardinality.ONE, command);
    }

    @Override
    public ResultSet execute(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.BINARY, Cardinality.MANY, query);
    }

    @Override
    public ResultSet queryJSON(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.JSON, Cardinality.MANY, query);
    }


    protected  <T extends BaseServerProtocol> PrepareComplete readPrepareComplete(IGranularFlowPipe granularFlowPipe, Prepare prepareMessage) throws IOException, EdgeDBInternalErrException, EdgeDBCommandException {
        log.debug("Reading prepare complete");
        BufferReader bufferReader = new BufferReaderImpl(getChannel());
        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();

        readBuffer = bufferReader.read(readBuffer);

        while (readBuffer.remaining()!= -1) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);

            T response = reader.read(readBuffer);

            if (response instanceof PrepareComplete) {
                log.debug("Response is an Instance Of PrepareComplete {}", (PrepareComplete) response);
                return (PrepareComplete) response;
            }

            if (response instanceof ErrorResponse) {
                log.debug("Response is an Instance Of Error {}", (ErrorResponse) response);
                ErrorResponse err = (ErrorResponse) response;
                throw new EdgeDBCommandException(err);
            }

            if (response instanceof ServerKeyData) {
                log.debug("Response is an Instance Of Error {}", (ServerKeyData) response);
                ServerKeyData serverKeyData = (ServerKeyData) response;
                this.serverKey = serverKeyData.getData();
                continue;
            }

            if (response instanceof ReadyForCommand) {
                log.debug("Response is an Instance Of ReadyForCommand {}", (ReadyForCommand) response);
                ReadyForCommand readyForCommand = (ReadyForCommand) response;

                switch (readyForCommand.getTransactionState()) {
                    case (int) IN_FAILED_TRANSACTION:
                        log.info("In Failed Transaction");
                        //TODO: Coding to concrete implementation here. Watch out.
                        SyncPipe syncPipe = new SyncPipeImpl(
                                new ChannelProtocolWriterImpl(getChannel()));
                        syncPipe.sendSyncMessage();
                        continue;
                    case (int) IN_TRANSACTION:
                        throw new UnsupportedOperationException();
                    case (int) NOT_IN_TRANSACTION:
                        log.info("Not In Transaction");
//                        granularFlowPipe.sendPrepareMessage(prepareMessage);
//                        return readPrepareComplete(granularFlowPipe,prepareMessage);
                        break;
                    default:
                        throw new EdgeDBInternalErrException(FAILED_TO_DECODE_SERVER_TRANSACTION_STATE);
                }

            }
        }

        if (readBuffer.remaining() == -1){
            // TODO: fix this
            throw new EdgeDBInternalErrException("Nothing to read in buffer");
        }
        return null;
    }

    protected ResultSet executeGranularFlow(char IOFormat, char cardinality, String command) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        IGranularFlowPipe granularFlowPipe = new GranularFlowPipeV2(
                new ChannelProtocolWriterImpl(getChannel()));

        Prepare prepareMessage = new Prepare(IOFormat, cardinality, command);
        granularFlowPipe.sendPrepareMessage(prepareMessage);
        PrepareComplete prepareComplete = readPrepareComplete(granularFlowPipe,prepareMessage);
        log.info("PrepareComplete received {}",prepareComplete);

//        try {
//            TypeDescriptor typeDescriptor = new TypeDecoderFactoryImpl().getTypeDescriptor(prepareComplete.getResultDataDescriptorID());
//        }catch (ScalarTypeNotFoundException e){
//
//        }
        granularFlowPipe.sendExecuteMessage(new Execute());
        return readDataResponse();
    }

    protected  <T extends BaseServerProtocol> ResultSet readDataResponse() throws IOException, EdgeDBInternalErrException {
        log.debug("Reading DataResponse");
        DataResponse dataResponse = null;
        BufferReader bufferReader = new BufferReaderImpl(getChannel());
        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();;
        readBuffer = bufferReader.read(readBuffer);

        ResultSet resultSet = new ResultSetImpl();
        while (true) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);
            T response = reader.read(readBuffer);

            if (response instanceof DataResponse) {
                log.debug("Response is an Instance Of DataResponse {}", (DataResponse) response);
                dataResponse = (DataResponse) response;
                resultSet.setResultData(dataResponse);
                log.debug("Data Response :- {}",dataResponse);
                return resultSet;
            }
        }

    }
}
