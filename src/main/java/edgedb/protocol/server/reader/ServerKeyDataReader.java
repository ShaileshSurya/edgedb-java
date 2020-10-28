package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.server.AuthenticationOK;
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
            log.debug("AVAILABLE ~~~~{}",String.valueOf(dataInputStream.available()));
            int messageLength = readerHelper.readUint32();
            readerHelper.setMessageLength(messageLength);
            serverKeyData.setMessageLength(messageLength);

            byte[] bytes= new byte[34];
            int readBytes = dataInputStream.read(bytes,0,32);
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
