package edgedb.internal.protocol.common;

import edgedb.internal.protocol.client.writer.BaseWriter;
import edgedb.internal.protocol.client.writerhelper.DataOutputStreamWriterHelperI;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataOutputStream;
import java.io.IOException;

@Data
@AllArgsConstructor
public class HeaderWriter extends BaseWriter {
    Header header;
    DataOutputStream dataOutputStream;

    @Override
    public void write() throws IOException {
        DataOutputStreamWriterHelperI dataOutputStreamWriterHelper = new DataOutputStreamWriterHelperI(dataOutputStream);
        dataOutputStreamWriterHelper.writeUint16(header.getCode());
    }

    @Override
    public void writeAndFlush() throws IOException {
        write();
        dataOutputStream.flush();
    }
}
