package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.server.ServerKeyData;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;

@Slf4j
public class ServerKeyDataReader extends BaseReader {
    public ServerKeyDataReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public ServerKeyDataReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public ServerKeyData read() throws IOException {
        ServerKeyData serverKeyData = new ServerKeyData();

        try{
            int messageLength = readerHelper.readUint32();
            readerHelper.setMessageLength(messageLength);
            serverKeyData.setMessageLength(messageLength);

            byte[] data = new byte[messageLength-4];
            int readBytes = readerHelper.getDataInputStream().read(data,0,32);
            log.debug(">>>>>>>>>readBytes>>>>>>>>>{}",readBytes);
            serverKeyData.setData(data);

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
