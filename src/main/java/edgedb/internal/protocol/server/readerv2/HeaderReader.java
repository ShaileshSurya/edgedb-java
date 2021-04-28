package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.Header;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class HeaderReader implements ProtocolReader {

    private IReaderHelper iReaderHelper;

    public Header read(ByteBuffer readBuffer) throws IOException {
        Header header = new Header();
        header.setCode(iReaderHelper.readUint16());
        header.setValue(iReaderHelper.readBytes());
        return header;
    }
}
