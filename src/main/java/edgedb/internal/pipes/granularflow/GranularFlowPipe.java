package edgedb.internal.pipes.granularflow;

import edgedb.client.SocketStream;
import edgedb.exceptions.EdgeDBCommandException;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.internal.pipes.BasePipe;
import edgedb.internal.protocol.client.*;
import edgedb.internal.protocol.client.writer.*;
import edgedb.internal.protocol.constants.Cardinality;
import edgedb.internal.protocol.constants.IOFormat;
import edgedb.internal.protocol.server.*;
import edgedb.internal.protocol.typedescriptor.BaseScalarType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class GranularFlowPipe extends BasePipe {
    BaseScalarType argumentType;
    BaseScalarType resultType;

    public GranularFlowPipe(SocketStream socketStream) {
        super(socketStream);
    }

    public void setup(String command) throws IOException, EdgeDBCommandException, EdgeDBInternalErrException {
        setup(new Prepare(IOFormat.JSON, Cardinality.MANY, command));
    }

    public void setup(String command, char cardinality, char ioFormat) throws IOException, EdgeDBCommandException, EdgeDBInternalErrException {
        setup(new Prepare(ioFormat, cardinality, command));
    }

    private void setup(Prepare prepare) throws IOException, EdgeDBInternalErrException, EdgeDBCommandException {
        write(new PrepareWriter(socketStream.getDataOutputStream(), prepare));
        writeAndFlush(new SyncMessageWriter(socketStream.getDataOutputStream(), buildSyncMessage()));

        PrepareComplete prepareComplete = readPrepareComplete();
        argumentType = prepareComplete.getArgumentDataDiscriptor();
        resultType = prepareComplete.getResultDataDescriptor();
    }

    public <T extends BaseServerProtocol> PrepareComplete readPrepareComplete() throws IOException, EdgeDBInternalErrException, EdgeDBCommandException {
        PrepareComplete prepareComplete = null;
        log.debug("Trying to read PrepareComplete");
        while (socketStream.getDataInputStream().available() > 0 || prepareComplete == null) {
            T response = readServerResponse();
            if (response instanceof PrepareComplete) {
                log.debug("Response is an Instance Of Preparecomplete {}", (PrepareComplete) response);
                prepareComplete = (PrepareComplete) response;
            }

            if (response instanceof ErrorResponse) {
                log.debug("Response is an Instance Of Error {}", (ErrorResponse) response);
                ErrorResponse err = (ErrorResponse) response;
                throw new EdgeDBCommandException(err);
            }

            if (response instanceof ReadyForCommand) {
                log.debug("Response is an Instance Of ReadyForCommand {}", (ReadyForCommand) response);
                ReadyForCommand readyForCommand = (ReadyForCommand) response;
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


    public <T extends BaseServerProtocol> DataResponse readExecuteResponse() throws IOException, EdgeDBInternalErrException {
        DataResponse dataResponse = null;
        while (socketStream.getDataInputStream().available() > 0 || dataResponse == null) {
            T response = readServerResponse();
            if (response instanceof DataResponse) {
                log.debug("Response is an Instance Of DataResponse {}", (DataResponse) response);
                dataResponse = (DataResponse) response;
            }
        }
        return dataResponse;
    }

    public Execute buildExecuteMessage() {
        return new Execute();
    }

    public SyncMessage buildSyncMessage() {
        return new SyncMessage();
    }

}
