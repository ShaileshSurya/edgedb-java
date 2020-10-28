package edgedb.protocol.common;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;

@Data
public class HeaderReader  {

    private DataInputStream dataInputStream;
    private ReaderHelper readerHelper;

    public HeaderReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        this.dataInputStream = dataInputStream;
        this.readerHelper= readerHelper;
    }

    public HeaderReader(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
        this.readerHelper = new ReaderHelper(dataInputStream);
    }

    public Header read() throws IOException {
        Header header = new Header();
        try {
            header.setCode(readerHelper.readUint16());
            header.setValue(readerHelper.readBytes());
            return header;
        } catch (OverReadException e) {
            return header;
        } catch (IOException e) {
            throw e;
        }
    }
}
