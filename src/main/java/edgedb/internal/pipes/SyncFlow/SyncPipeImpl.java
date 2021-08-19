package edgedb.internal.pipes.SyncFlow;

import edgedb.internal.protocol.SyncMessage;
import edgedb.internal.protocol.client.writerV2.ProtocolWritable;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class SyncPipeImpl implements SyncPipe {

    ProtocolWritable writer;

    @Override
    public void sendSyncMessage() {
        writer.write(new SyncMessage());
    }
}
