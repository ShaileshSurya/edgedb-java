package edgedb.internal.protocol.client.writer;

import edgedb.internal.protocol.client.Prepare;
import edgedb.internal.protocol.client.writerhelper.WriteHelper;
import edgedb.internal.protocol.common.HeaderWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class PrepareWriter extends BaseWriter {

    DataOutputStream dataOutputStream;
    Prepare prepare;

    public void write() throws IOException {
        log.debug("PrepareWriter {}", prepare.toString());
        WriteHelper writerHelper = new WriteHelper(dataOutputStream);
        writerHelper.writeUint8(prepare.getMType());
        writerHelper.writeUint32(prepare.getMessageLength());

        int headersLength = prepare.getHeadersLength();
        writerHelper.writeUint16(headersLength);
        for (int i = 0; i < prepare.getHeadersLength(); i++) {
            new HeaderWriter(prepare.getHeaders()[i], dataOutputStream);
        }

        writerHelper.writeUint8(prepare.getIoFormat());
        writerHelper.writeUint8(prepare.getExpectedCardinality());
        writerHelper.writeBytes(prepare.getStatementName());
        writerHelper.writeString(prepare.getCommand());
    }

    public void writeAndFlush() throws IOException {
        write();
        dataOutputStream.flush();
    }
}
