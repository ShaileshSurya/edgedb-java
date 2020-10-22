package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.client.Header;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;

@Data
public class HeaderReader extends BaseReader {

    public HeaderReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public HeaderReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public Header read() throws IOException {
        Header header = new Header();
        try {
            header.setCode(readerHelper.readUint16());
            header.setValue(readerHelper.readByteArray());
            return header;
        } catch (OverReadException e) {
            return header;
        } catch (IOException e) {
            throw e;
        }
    }
}
