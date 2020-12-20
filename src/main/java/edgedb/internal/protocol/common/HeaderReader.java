package edgedb.internal.protocol.common;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import edgedb.internal.protocol.server.readerv2.ProtocolReader;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class HeaderReader implements ProtocolReader {


    private DataInputStream dataInputStream;
    private ReaderHelper readerHelper;

    private IReaderHelper iReaderHelper;

    public HeaderReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        this.dataInputStream = dataInputStream;
        this.readerHelper = readerHelper;
    }

    public HeaderReader(IReaderHelper iReaderHelper){
        this.iReaderHelper = iReaderHelper;
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
    public Header read(ByteBuffer readBuffer) throws IOException {
        Header header = new Header();
        try {
            header.setCode(iReaderHelper.readUint16());
            header.setValue(iReaderHelper.readBytes());
            return header;
        } catch (OverReadException e) {
            return header;
        } catch (IOException e) {
            throw e;
        }
    }
}
