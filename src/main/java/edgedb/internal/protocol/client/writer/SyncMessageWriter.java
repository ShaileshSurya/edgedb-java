package edgedb.internal.protocol.client.writer;

import edgedb.internal.protocol.client.SyncMessage;
import edgedb.internal.protocol.client.writerhelper.DataOutputStreamWriterHelperI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class SyncMessageWriter extends BaseWriter {

    DataOutputStream dataOutputStream;
    SyncMessage syncMessage;

    public void write() throws IOException {
        log.debug("SyncMessageWriter {}", syncMessage.toString());
        DataOutputStreamWriterHelperI writerHelper = new DataOutputStreamWriterHelperI(dataOutputStream);
        writerHelper.writeUint8(syncMessage.getMType());
        writerHelper.writeUint32(syncMessage.getMessageLength());
    }

    public void writeAndFlush() throws IOException {
        write();
        dataOutputStream.flush();
    }
}
