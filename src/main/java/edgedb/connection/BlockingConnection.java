package edgedb.connection;

import edgedb.client.ResultSet;
import edgedb.client.ResultSetImpl;
import edgedb.connectionparams.ConnectionParams;
import edgedb.exceptions.*;
import edgedb.exceptions.clientexception.ClientException;
import edgedb.exceptions.constants.Severity;
import edgedb.internal.buffer.SingletonBuffer;
import edgedb.internal.pipes.SyncFlow.SyncPipe;
import edgedb.internal.pipes.SyncFlow.SyncPipeImpl;
import edgedb.internal.pipes.authenticationflow.AutheticationFlowScramSASL;
import edgedb.internal.pipes.authenticationflow.IScramSASLAuthenticationFlow;
import edgedb.internal.pipes.connect.ConnectionPipe;
import edgedb.internal.pipes.connect.IConnectionPipe;
import edgedb.internal.pipes.granularflow.GranularFlowPipeV2;
import edgedb.internal.pipes.granularflow.IGranularFlowPipe;
import edgedb.internal.pipes.scriptFlow.IScriptFlow;
import edgedb.internal.pipes.scriptFlow.ScriptFlow;
import edgedb.internal.protocol.*;
import edgedb.internal.protocol.client.writerV2.ChannelProtocolWritableImpl;
import edgedb.internal.protocol.constants.Cardinality;
import edgedb.internal.protocol.constants.IOFormat;
import edgedb.internal.protocol.server.readerfactory.ChannelProtocolReaderFactoryImpl;
import edgedb.internal.protocol.server.readerv2.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static edgedb.client.ClientConstants.MAJOR_VERSION;
import static edgedb.client.ClientConstants.MINOR_VERSION;
import static edgedb.exceptions.constants.ClientErrors.INCOMPATIBLE_DRIVER;
import static edgedb.internal.protocol.constants.TransactionState.*;

@Slf4j
public class BlockingConnection implements IConnection {

    SocketChannel clientChannel;

    // TODO: this has become ugly. Need to refactor with the connection builder
    ConnectionParams connectionParams;
    byte[] serverKey;

    @Override
    public ResultSet query(String query) {
        try {
            return executeGranularFlow(IOFormat.BINARY, Cardinality.MANY, query);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet queryOne(String command) {
        try {
            return executeGranularFlow(IOFormat.BINARY, Cardinality.ONE, command);
        } catch (Exception ex) {
            log.info("Error Here {}",ex);
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void execute(String command) {
        try {
            executeScript(command);
        } catch (IOException ex) {
            log.info("Error Here {}",ex);
            ex.printStackTrace();
        }
    }

    private <T extends ServerProtocolBehaviour> void executeScript(String query) throws IOException {

        ExecuteScript executeScript = new ExecuteScript(query);
        IScriptFlow scriptFlow = new ScriptFlow(new ChannelProtocolWritableImpl(getChannel()));

        scriptFlow.executeScriptMessage(executeScript);

        BufferReader bufferReader = new BufferReaderImpl(clientChannel);
        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();

        readBuffer = bufferReader.read(readBuffer);

        while (readBuffer.remaining() != -1) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);
            T response = reader.read(readBuffer);
            if (response instanceof CommandComplete) {
                return;
            } else if (response instanceof ErrorResponse) {
                throw IExceptionFromErrorResponseBuilderImpl.getExceptionFromError((ErrorResponse) response);
            }
        }
    }

    @Override
    public ResultSet queryJSON(String query) throws EdgeDBQueryException, EdgeDBCommandException, IOException, EdgeDBInternalErrException {
        return executeGranularFlow(IOFormat.JSON, Cardinality.MANY, query);
    }


    protected <T extends ServerProtocolBehaviour> PrepareComplete readPrepareComplete(IGranularFlowPipe granularFlowPipe, Prepare prepareMessage) throws IOException {
        log.debug("Reading prepare complete");
        BufferReader bufferReader = new BufferReaderImpl(clientChannel);
        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();

        readBuffer = bufferReader.read(readBuffer);

        while (readBuffer.remaining() != -1) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);

            T response = reader.read(readBuffer);

            if (response instanceof PrepareComplete) {
                log.debug("Response is an Instance Of PrepareComplete {}", (PrepareComplete) response);
                return (PrepareComplete) response;
            }

            if (response instanceof ErrorResponse) {
                throw IExceptionFromErrorResponseBuilderImpl.getExceptionFromError((ErrorResponse) response);
            }

            if (response instanceof ServerKeyDataBehaviour) {
                log.debug("Response is an Instance Of Error {}", (ServerKeyDataBehaviour) response);
                ServerKeyDataBehaviour serverKeyData = (ServerKeyDataBehaviour) response;
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
                                new ChannelProtocolWritableImpl(getChannel()));
                        syncPipe.sendSyncMessage();
                        continue;
                    case (int) IN_TRANSACTION:
                        log.info("In Transaction");
                        throw new UnsupportedOperationException();
                    case (int) NOT_IN_TRANSACTION:
                        log.info("Not In Transaction");
                        break;
                }

            }
        }

        return null;
    }

    protected ResultSet executeGranularFlow(char IOFormat, char cardinality, String command) throws IOException {
        IGranularFlowPipe granularFlowPipe = new GranularFlowPipeV2(
                new ChannelProtocolWritableImpl(getChannel()));

        Prepare prepareMessage = new Prepare(IOFormat, cardinality, command);
        granularFlowPipe.sendPrepareMessage(prepareMessage);
        PrepareComplete prepareComplete = readPrepareComplete(granularFlowPipe, prepareMessage);
        log.info("PrepareComplete received {}", prepareComplete);
//        try {
//            TypeDescriptor typeDescriptor = new TypeDecoderFactoryImpl().getTypeDescriptor(prepareComplete.getResultDataDescriptorID());
//        }catch (ScalarTypeNotFoundException e){
//
//        }
        granularFlowPipe.sendExecuteMessage(new Execute());
        return readDataResponse();
    }

    protected <T extends ServerProtocolBehaviour> ResultSet readDataResponse() throws IOException {
        log.debug("Reading DataResponse");
        DataResponse dataResponse;
        BufferReader bufferReader = new BufferReaderImpl(getChannel());
        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();

        readBuffer = bufferReader.read(readBuffer);

        ResultSet resultSet = new ResultSetImpl();
        while (readBuffer.hasRemaining()) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);
            T response = reader.read(readBuffer);

            if (response instanceof DataResponse) {
                log.debug("Response is an Instance Of DataResponse {}", (DataResponse) response);
                dataResponse = (DataResponse) response;

                resultSet.setResultData(dataResponse);

                log.debug("Data Response :- {}", dataResponse);
            }
        }
        return resultSet;

    }


    @Override
    public void terminate() throws IOException {
        IConnectionPipe connectionPipeV2 = new ConnectionPipe(
                new ChannelProtocolWritableImpl(getChannel()));

        connectionPipeV2.sendTerminate(new Terminate());
    }

    public IConnection createClientSocket(ConnectionParams params) throws IOException {

        log.info("Trying to create Client Socket");
        this.connectionParams = params;
        clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(true);
        if (!clientChannel.connect(new InetSocketAddress(connectionParams.getHost(), connectionParams.getPort()))) {
            log.info("Trying to connect ...");
            while (!clientChannel.finishConnect()) {
                ;
            }

            log.info("Connection Successful....");
        }
        return this;
    }

    public void initiateHandshake(String user, String database) throws IOException {
        log.info("Initiating Client Handshake");
        ClientHandshake clientHandshakeMessage = new ClientHandshake(user, database);
        IConnectionPipe connectionPipeV2 = new ConnectionPipe(
                new ChannelProtocolWritableImpl(getChannel()));
        connectionPipeV2.sendClientHandshake(clientHandshakeMessage);
    }

    public void handleHandshake() throws IOException {
        tryHandleHandshake();
        log.info("Connection Successful, Ready for command.");
    }

    private <T extends ServerProtocolBehaviour> void tryHandleHandshake() throws IOException {
        log.debug("Trying to read response for client handshake");

        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();
        BufferReader bufferReader = new BufferReaderImpl(getChannel());
        readBuffer = bufferReader.read(readBuffer);


        while (readBuffer.hasRemaining()) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);

            T response = reader.read(readBuffer);
            log.info(" Response Found was {}", response.toString());
            if (response instanceof ServerHandshakeBehaviour) {
                ServerHandshakeBehaviour serverHandshake = (ServerHandshakeBehaviour) response;
                log.debug("Response is an Instance Of ServerHandshake {}", serverHandshake);
                String message = String.format("Incompatible driver expected Minor Version %s and Major Version %s,found Minor Version %s and Major Version %s", MAJOR_VERSION, MINOR_VERSION, serverHandshake.getMajorVersion(), serverHandshake.getMinorVersion());
                throw new ClientException(INCOMPATIBLE_DRIVER, message, Severity.ERROR);
            } else if (response instanceof ReadyForCommand) {
                ReadyForCommand readyForCommand = (ReadyForCommand) response;
                log.debug("Response is an Instance Of ReadyForCommand {}", readyForCommand);

                switch (readyForCommand.getTransactionState()) {
                    case (int) IN_FAILED_TRANSACTION:
                        SyncPipe syncPipe = new SyncPipeImpl(
                                new ChannelProtocolWritableImpl(getChannel()));
                        syncPipe.sendSyncMessage();
                        break;
                    case (int) IN_TRANSACTION:
                        break;
                    case (int) NOT_IN_TRANSACTION:
                        return;
                }
            } else if (response instanceof ServerAuthenticationBehaviour) {
                ServerAuthenticationBehaviour serverAuthenticationBehaviour = (ServerAuthenticationBehaviour) response;

                if (serverAuthenticationBehaviour.isAuthenticationOkMessage()) {
                    log.info("Authentication Successful");
                    return;
                } else if (serverAuthenticationBehaviour.isAuthenticationRequiredSASLMessage()) {
                    authenticateSASL();
                    continue;
                }
            } else if (response instanceof ServerKeyDataBehaviour) {
                continue;
            }
        }
    }


    public <T extends ServerProtocolBehaviour> void authenticateSASL() throws IOException {

        IScramSASLAuthenticationFlow authenticationFlow = new AutheticationFlowScramSASL(
                new ChannelProtocolWritableImpl(getChannel()));
        authenticationFlow.sendAuthenticationSASLClientFirstMessage(connectionParams.getUser());

        ByteBuffer readBuffer = SingletonBuffer.getInstance().getBuffer();
        BufferReader bufferReader = new BufferReaderImpl(getChannel());
        readBuffer = bufferReader.read(readBuffer);


        while (readBuffer.hasRemaining()) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);

            T response = reader.read(readBuffer);

            log.info("Response Found was {}", response.toString());
            if (response instanceof ServerAuthenticationBehaviour) {
                ServerAuthenticationBehaviour serverAuthenticationBehaviour = (ServerAuthenticationBehaviour) response;
                if (serverAuthenticationBehaviour.isAuthenticationSASLContinueMessage()) {
                    authenticationFlow.sendAuthenticationSASLClientFinalMessage(serverAuthenticationBehaviour, connectionParams.getPassword());
                    continue;
                }
            } else if (response instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse) response;
                throw IExceptionFromErrorResponseBuilderImpl.getExceptionFromError(errorResponse);
            }

        }

        readClientFinalMessage(readBuffer, bufferReader);
    }

    private <T extends ServerProtocolBehaviour> void readClientFinalMessage(ByteBuffer readBuffer, BufferReader bufferReader) throws IOException {
        readBuffer = bufferReader.read(readBuffer);
        while (readBuffer.hasRemaining()) {
            byte mType = readBuffer.get();
            ProtocolReader reader = new ChannelProtocolReaderFactoryImpl(readBuffer)
                    .getProtocolReader((char) mType, readBuffer);

            T response = reader.read(readBuffer);

            log.info("Response Found was {}", response.toString());
            if (response instanceof ServerAuthenticationBehaviour) {
                ServerAuthenticationBehaviour serverAuthenticationBehaviour = (ServerAuthenticationBehaviour) response;

                if (serverAuthenticationBehaviour.isAuthenticationSASLFinal()) {
                    // Expect AuthenticationOk message
                    continue;
                } else if (serverAuthenticationBehaviour.isAuthenticationOkMessage()) {
                    break;
                }
            } else if (response instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse) response;
                throw IExceptionFromErrorResponseBuilderImpl.getExceptionFromError(errorResponse);
            }

        }
    }

    @Override
    public SocketChannel getChannel() {
        return this.clientChannel;
    }
}
