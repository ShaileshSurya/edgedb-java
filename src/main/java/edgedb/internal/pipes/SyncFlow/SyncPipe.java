package edgedb.internal.pipes.SyncFlow;

import java.io.IOException;

public interface SyncPipe {
    public void sendSyncMessage() throws IOException;
}
