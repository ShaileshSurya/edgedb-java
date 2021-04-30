package edgedb.internal.pipes.granularflow;

import edgedb.internal.protocol.ExecuteMessage;
import edgedb.internal.protocol.Prepare;

import java.io.IOException;


public interface IGranularFlowPipe {
    public void sendPrepareMessage(Prepare prepareMessage) throws IOException;
    public void sendDescribeStatementMessage();
    public void sendExecuteMessage(ExecuteMessage executeMessage) throws IOException;
    public void sendOptimisticExecuteMessage();
}
