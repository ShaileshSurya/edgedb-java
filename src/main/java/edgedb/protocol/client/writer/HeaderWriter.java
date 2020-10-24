package edgedb.protocol.client.writer;

import edgedb.protocol.client.Header;
import edgedb.protocol.client.writerhelper.WriteHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataOutputStream;
import java.io.IOException;

@Data
@AllArgsConstructor
public class HeaderWriter implements Write {
    Header header;
    DataOutputStream dataOutputStream;

    @Override
    public void write() throws IOException {
        WriteHelper writeHelper = new WriteHelper(dataOutputStream);
        writeHelper.writeUint16(header.getCode());
    }
}
