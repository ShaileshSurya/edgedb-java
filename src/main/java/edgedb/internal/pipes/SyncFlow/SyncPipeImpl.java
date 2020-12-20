package edgedb.internal.pipes.SyncFlow;

import edgedb.internal.protocol.client.SyncMessage;
import edgedb.internal.protocol.client.writerV2.ProtocolWriter;
import edgedb.internal.protocol.server.readerv2.*;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;

import static edgedb.internal.protocol.constants.CommonConstants.BUFFER_SIZE;

@AllArgsConstructor
public class SyncPipeImpl implements SyncPipe {

    ProtocolWriter writer;

    @Override
    public void sendSyncMessage() throws IOException {
        writer.write(new SyncMessage());
    }
}
