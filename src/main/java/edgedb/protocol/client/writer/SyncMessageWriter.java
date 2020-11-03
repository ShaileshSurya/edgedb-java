package edgedb.protocol.client.writer;

import edgedb.protocol.client.SyncMessage;
import edgedb.protocol.client.writerhelper.WriteHelper;
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
        WriteHelper writerHelper = new WriteHelper(dataOutputStream);
        writerHelper.writeUint8(syncMessage.getMType());
        writerHelper.writeUint32(syncMessage.getMessageLength());
    }

    public void writeAndFlush() throws IOException {
        write();
        dataOutputStream.flush();
    }
}
