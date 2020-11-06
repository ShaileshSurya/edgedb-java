package edgedb.internal.protocol.client.writer;

import edgedb.internal.protocol.client.ClientHandshake;
import edgedb.internal.protocol.client.ConnectionParams;
import edgedb.internal.protocol.client.writerhelper.WriteHelperV2;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Slf4j
@AllArgsConstructor
public class ClientHandshakeWriterV2 {

    SocketChannel clientChannel;
    ClientHandshake clientHandshake;

    public ByteBuffer buildWriteBuffer() throws IOException {
        ByteBuffer writeBuf = ByteBuffer.allocate(clientHandshake.getMessageLength()+1);

        WriteHelperV2 writeHelper = new WriteHelperV2(writeBuf);

        log.debug("ClientHandshakeWriter {}", clientHandshake.toString());
        writeHelper.writeUint8(clientHandshake.getMType());
        writeHelper.writeUint32(clientHandshake.getMessageLength());
        writeHelper.writeUint16(clientHandshake.getMajorVersion());
        writeHelper.writeUint16(clientHandshake.getMinorVersion());
        writeHelper.writeUint16(clientHandshake.getConnectionParamLength());
        for (int i = 0; i < clientHandshake.getConnectionParamLength(); i++) {
            ConnectionParams param = clientHandshake.getConnectionParams()[i];
            writeHelper.writeString(param.getName());
            writeHelper.writeString(param.getValue());
        }
        writeHelper.writeUint16(clientHandshake.getProtocolExtensionLength());
        return writeBuf;
    }

    public void write() throws IOException {
        ByteBuffer writeBuf = buildWriteBuffer();
        writeBuf.flip();
        while(writeBuf.hasRemaining()){
            clientChannel.write(writeBuf);
        }
    }

//    public void writeAndFlush() throws IOException {
//        write();
//        dataOutputStream.flush();
//    }
}
