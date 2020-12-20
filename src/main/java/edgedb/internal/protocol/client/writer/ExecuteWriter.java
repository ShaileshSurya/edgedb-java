package edgedb.internal.protocol.client.writer;

import edgedb.internal.protocol.client.Execute;
import edgedb.internal.protocol.client.writerhelper.DataOutputStreamWriterHelperI;
import edgedb.internal.protocol.common.HeaderWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class ExecuteWriter extends BaseWriter {
    DataOutputStream dataOutputStream;
    Execute execute;


    @Override
    public void write() throws IOException {
        log.debug("ExecuteWriter {}", execute);

        DataOutputStreamWriterHelperI write = new DataOutputStreamWriterHelperI(dataOutputStream);
        write.writeUint8(execute.getMType());
        write.writeUint32(execute.getMessageLength());


        int headersLength = execute.getHeadersLength();
        write.writeUint16(headersLength);
        for (int i = 0; i < execute.getHeadersLength(); i++) {
            new HeaderWriter(execute.getHeaders()[i], dataOutputStream);
        }

        write.writeBytes("".getBytes());

        //TODO: this needs to be fixed
        write.writeUint32(4);
        write.writeUint32(0);
    }

    public void writeAndFlush() {

    }
}
