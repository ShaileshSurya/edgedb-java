package edgedb.internal.protocol.server.readerv2;

import edgedb.internal.protocol.ServerKeyDataBehaviour;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
@AllArgsConstructor
public class ServerKeyDataReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    public ServerKeyDataBehaviour read(ByteBuffer buffer) {
        ServerKeyDataBehaviour serverKeyData = new ServerKeyDataBehaviour();

        int messageLength = readerHelper.readUint32();
        readerHelper.setMessageLength(messageLength);
        serverKeyData.setMessageLength(messageLength);

        byte[] bytes = new byte[34];
        bytes = readerHelper.read(bytes, 0, 32);
        serverKeyData.setData(bytes);

        return serverKeyData;

    }
}
