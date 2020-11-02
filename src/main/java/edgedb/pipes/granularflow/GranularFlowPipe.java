package edgedb.pipes.granularflow;

import edgedb.client.SocketStream;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.pipes.BasePipe;
import edgedb.protocol.client.*;
import edgedb.protocol.client.writer.*;
import edgedb.protocol.constants.Cardinality;
import edgedb.protocol.constants.IOFormat;
import edgedb.protocol.server.*;

import edgedb.protocol.typedescriptor.BaseScalarType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class GranularFlowPipe extends BasePipe {
    BaseScalarType argumentType;
    BaseScalarType resultType;

    public GranularFlowPipe(SocketStream socketStream) {
        super(socketStream);
    }

    public void setup(String command) throws IOException, EdgeDBInternalErrException {
        write(new PrepareWriter(socketStream.getDataOutputStream(), buildPrepareMessage(command)));
        writeAndFlush(new SyncMessageWriter(socketStream.getDataOutputStream(), buildSyncMessage()));

        PrepareComplete prepareComplete = readPrepareComplete();
        argumentType = prepareComplete.getArgumentDataDiscriptor();
        resultType = prepareComplete.getResultDataDescriptor();
    }

    public <T extends BaseServerProtocol> PrepareComplete readPrepareComplete() throws IOException {
        PrepareComplete prepareComplete= null;
        while (socketStream.getDataInputStream().available() > 0 || prepareComplete == null) {
            T response = readServerResponse();
            if (response instanceof PrepareComplete) {
                log.debug("Response is an Instance Of Preparecomplete {}",(PrepareComplete) response);
                prepareComplete = (PrepareComplete) response;
            }
        }
        return prepareComplete;
    }

    public String execute() throws IOException, EdgeDBInternalErrException {
        log.info("Trying to execute in granular flow");
        write(new ExecuteWriter(socketStream.getDataOutputStream(), buildExecuteMessage()));
        writeAndFlush(new SyncMessageWriter(socketStream.getDataOutputStream(), buildSyncMessage()));

        DataResponse response = readExecuteResponse();
        byte[] responseByteArray = response.getDataElements()[0].getDataElement();
        return new String(responseByteArray);
    }


    public <T extends BaseServerProtocol> DataResponse readExecuteResponse() throws IOException {
        DataResponse dataResponse= null;
        while (socketStream.getDataInputStream().available() > 0 || dataResponse == null) {
            T response = readServerResponse();
            if (response instanceof DataResponse) {
                log.debug("Response is an Instance Of DataResponse {}",(DataResponse) response);
                dataResponse = (DataResponse) response;
            }
        }
        return dataResponse;
    }

    public Execute buildExecuteMessage() {
        return new Execute();
    }

    public Prepare buildPrepareMessage(String command) {
        return new Prepare(IOFormat.JSON, Cardinality.MANY, command);
    }


    public SyncMessage buildSyncMessage() {
        return new SyncMessage();
    }

}
