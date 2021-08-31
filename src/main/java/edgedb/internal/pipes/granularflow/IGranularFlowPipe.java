package edgedb.internal.pipes.granularflow;

import edgedb.internal.protocol.Execute;
import edgedb.internal.protocol.Prepare;

import java.io.IOException;


public interface IGranularFlowPipe {
    public void sendPrepareMessage(Prepare prepareMessage);
    public void sendDescribeStatementMessage();
    public void sendExecuteMessage(Execute executeMessage);
    public void sendOptimisticExecuteMessage();
}
