package edgedb.internal.pipes.granularflow;

import edgedb.internal.protocol.client.*;
import edgedb.internal.protocol.client.writerV2.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class GranularFlowPipeV2 implements IGranularFlowPipe{

    ProtocolWriter protocolWriter;

    @Override
    public void sendPrepareMessage(Prepare prepareMessage) throws IOException {
        log.debug("Sending prepare message {}",prepareMessage);
        protocolWriter.write(new SyncBufferWriterDecorator<>(prepareMessage));
    }

    @Override
    public void sendDescribeStatementMessage() {

    }

    @Override
    public void sendExecuteMessage(Execute executeMessage) throws IOException {
        log.debug("Sending execute message {}",executeMessage);
        protocolWriter.write(new SyncBufferWriterDecorator<>(executeMessage));
    }

    @Override
    public void sendOptimisticExecuteMessage() {

    }
}
