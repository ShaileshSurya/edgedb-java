package edgedb.protocol.common;

import edgedb.protocol.client.writer.BaseWriter;
import edgedb.protocol.common.Header;
import edgedb.protocol.client.writerhelper.WriteHelper;
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
        WriteHelper writeHelper = new WriteHelper(dataOutputStream);
        writeHelper.writeUint16(header.getCode());
    }

    @Override
    public void writeAndFlush() throws IOException {
        write();
        dataOutputStream.flush();
    }
}
