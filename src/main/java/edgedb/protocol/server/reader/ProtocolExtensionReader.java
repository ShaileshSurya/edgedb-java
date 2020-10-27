package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.client.Header;
import edgedb.protocol.client.ProtocolExtension;
import edgedb.protocol.server.readerhelper.ReaderHelper;

import java.io.DataInputStream;
import java.io.IOException;

public class ProtocolExtensionReader extends BaseReader {
    public ProtocolExtensionReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public ProtocolExtensionReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public ProtocolExtension read() throws IOException {
        ProtocolExtension protocolExtension = new ProtocolExtension();

        try {
            protocolExtension.setName(readerHelper.readString());

            // This is violating DRY.
            short headersLength = readerHelper.readUint16();
            protocolExtension.setHeadersLength(headersLength);
            Header[] headers = new Header[headersLength];
            HeaderReader headerReader = new HeaderReader(dataInputStream, this.readerHelper);
            for (int i = 0; i < headersLength; i++) {
                headers[i] = headerReader.read();
            }

            return protocolExtension;
        } catch (OverReadException e) {
            return protocolExtension;
        } catch (IOException e) {
            throw e;
        }
    }
}
