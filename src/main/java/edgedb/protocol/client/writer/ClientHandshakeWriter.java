package edgedb.protocol.client.writer;


import edgedb.protocol.client.ClientHandshake;
import edgedb.protocol.client.ConnectionParams;
import edgedb.protocol.client.writerhelper.WriteHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class ClientHandshakeWriter implements Write {
    DataOutputStream dataOutputStream;
    ClientHandshake clientHandshake;

    public void write() throws IOException {
        log.debug("ClientHandshakeWriter {}", clientHandshake.toString());
        WriteHelper write = new WriteHelper(dataOutputStream);
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
        dataOutputStream.flush();
    }

}
