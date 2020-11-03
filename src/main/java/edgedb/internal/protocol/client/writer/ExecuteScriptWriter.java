package edgedb.internal.protocol.client.writer;

import edgedb.internal.protocol.client.ExecuteScript;
import edgedb.internal.protocol.client.writerhelper.WriteHelper;
import edgedb.internal.protocol.common.HeaderWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@AllArgsConstructor
@Data
@Slf4j
public class ExecuteScriptWriter extends BaseWriter {

    ExecuteScript executeScript;
    DataOutputStream dataOutputStream;

    @Override
    public void write() throws IOException {
        WriteHelper write = new WriteHelper(dataOutputStream);
        log.debug("Writing Execute Script");
        write.writeUint8(executeScript.getMType());
        write.writeUint32(executeScript.getMessageLength());
        write.writeUint16(executeScript.getHeadersLength());

        for (int i = 0; i < executeScript.getHeadersLength(); i++) {
            new HeaderWriter(executeScript.getHeaders()[i], dataOutputStream);
        }

        write.writeString(executeScript.getScript());
        dataOutputStream.flush();
        log.debug("Writing Execute Script succesfull");
    }

    @Override
    public void writeAndFlush() throws IOException {

    }
}
