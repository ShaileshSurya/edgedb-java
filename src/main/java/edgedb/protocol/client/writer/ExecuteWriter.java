package edgedb.protocol.client.writer;

import edgedb.protocol.client.*;
import edgedb.protocol.client.writerhelper.WriteHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class ExecuteWriter implements Write{
    DataOutputStream dataOutputStream;
    Execute execute;


    @Override
    public void write() throws IOException {
        log.debug("ExecuteWriter {}",execute);

        WriteHelper write = new WriteHelper(dataOutputStream);
        write.writeUint8(execute.getMType());
        write.writeUint32(execute.getMessageLength());


        int headersLength = execute.getHeadersLength();
        write.writeUint16(headersLength);
        for (int i = 0; i < execute.getHeadersLength(); i++) {
            new HeaderWriter(execute.getHeaders()[i], dataOutputStream);
        }

        write.writeBytes("".getBytes());

        write.writeUint32(4);
        write.writeUint32(0);
    }

    public void writeAndFlush(){

    }
}
