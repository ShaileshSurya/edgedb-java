package edgedb.protocol.client.writer;

import edgedb.protocol.client.ClientHandshake;
import edgedb.protocol.client.ConnectionParams;
import edgedb.protocol.client.writerhelper.WriteHelper;

import java.io.*;

public class ClientHandshakeWriter implements Write {
    DataOutputStream dataOutputStream;
    ClientHandshake clientHandshake;

    public ClientHandshakeWriter(ClientHandshake clientHandshake, DataOutputStream dataOutputStream){
            this.dataOutputStream = dataOutputStream;
            this.clientHandshake = clientHandshake;
    }

    public void encode() throws IOException {
        edgedb.protocol.client.writerhelper.Write write = new WriteHelper(dataOutputStream);
        write.writeUint8(clientHandshake.getMType());
        write.writeUint32(clientHandshake.getMessageLength());
        write.writeUint16(clientHandshake.getMajorVersion());
        write.writeUint16(clientHandshake.getMinorVersion());
        write.writeUint16(clientHandshake.getConnectionParamLength());
        for (int i= 0;i<clientHandshake.getConnectionParamLength();i++){
            ConnectionParams param = clientHandshake.getConnectionParams()[i];
            write.writeString(param.getName());
            write.writeString(param.getValue());
        }
        write.writeUint16(clientHandshake.getProtocolExtensionLength());
    }

}
