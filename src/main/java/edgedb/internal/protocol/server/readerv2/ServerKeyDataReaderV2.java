package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.server.BaseServerProtocol;
import edgedb.internal.protocol.server.ServerKeyData;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
@AllArgsConstructor
public class ServerKeyDataReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    public ServerKeyData read(ByteBuffer buffer) throws IOException {
        ServerKeyData serverKeyData = new ServerKeyData();

        try {
            int messageLength = readerHelper.readUint32();
            readerHelper.setMessageLength(messageLength);
            serverKeyData.setMessageLength(messageLength);

            byte[] bytes = new byte[34];
            bytes = readerHelper.read(bytes, 0, 32);
            serverKeyData.setData(bytes);

            return serverKeyData;
        } catch (OverReadException e) {
            e.printStackTrace();
            return serverKeyData;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
