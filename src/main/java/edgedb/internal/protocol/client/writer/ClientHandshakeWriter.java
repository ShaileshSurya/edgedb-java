package edgedb.internal.protocol.client.writer;


import edgedb.internal.protocol.client.ClientHandshake;
import edgedb.internal.protocol.client.ConnectionParams;
import edgedb.internal.protocol.client.writerhelper.DataOutputStreamWriterHelperI;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class ClientHandshakeWriter extends BaseWriter {
    DataOutputStream dataOutputStream;
    ClientHandshake clientHandshake;

    public void write() throws IOException {
        log.debug("ClientHandshakeWriter {}", clientHandshake.toString());
        DataOutputStreamWriterHelperI write = new DataOutputStreamWriterHelperI(dataOutputStream);
        write.writeUint8(clientHandshake.getMType());
        write.writeUint32(clientHandshake.getMessageLength());
        write.writeUint16(clientHandshake.getMajorVersion());
        write.writeUint16(clientHandshake.getMinorVersion());
        write.writeUint16(clientHandshake.getConnectionParamLength());
        for (int i = 0; i < clientHandshake.getConnectionParamLength(); i++) {
            ConnectionParams param = clientHandshake.getConnectionParams()[i];
            write.writeString(param.getName());
            write.writeString(param.getValue());
        }
        write.writeUint16(clientHandshake.getProtocolExtensionLength());

    }

    @Override
    public void writeAndFlush() throws IOException {
        write();
        dataOutputStream.flush();
    }

}
