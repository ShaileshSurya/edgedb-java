package edgedb.internal.protocol.client.writer;

import edgedb.internal.protocol.client.Terminate;
import edgedb.internal.protocol.client.writerhelper.DataOutputStreamWriterHelperI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class TerminateWriter extends BaseWriter {

    DataOutputStream dataOutputStream;
    Terminate terminate;

    public void write() throws IOException {
        log.debug("Writing terminate Message {}", terminate);
        DataOutputStreamWriterHelperI helper = new DataOutputStreamWriterHelperI(dataOutputStream);

        helper.writeUint8(terminate.getMType());
        helper.writeUint32(terminate.getMessageLength());
    }

    public void writeAndFlush() throws IOException {
        write();
        dataOutputStream.flush();
    }
}
