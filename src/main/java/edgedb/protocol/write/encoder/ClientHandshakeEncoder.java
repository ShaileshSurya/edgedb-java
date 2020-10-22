package edgedb.protocol.write.encoder;

import edgedb.protocol.write.ClientHandshake;

import java.io.*;

public class ClientHandshakeEncoder implements Encoder {
    DataOutputStream dataOutputStream;
    ClientHandshake clientHandshake;

    public ClientHandshakeEncoder(ClientHandshake clientHandshake, DataOutputStream dataOutputStream){
            this.dataOutputStream = dataOutputStream;
            this.clientHandshake = clientHandshake;
    }

    public void encode() throws IOException {
//        Write write = new Writer(dataOutputStream);
//        write.writeUint8(clientHandshake.getMType());
//        write.writeUint32(clientHandshake.getMessageLength());
//        write.writeUint16(clientHandshake.getMajorVersion());
//        write.writeUint16(clientHandshake.getMinorVersion());
//        write.writeUint16(clientHandshake.getConnectionParamLength());
//        for (int i= 0;i<clientHandshake.getConnectionParamLength();i++){
//            ConnectionParams param = clientHandshake.getConnectionParams()[i];
//            write.writeString(param.getName());
//            write.writeString(param.getValue());
//        }
//        write.writeUint16(clientHandshake.getProtocolExtensionLength());
    }

}
