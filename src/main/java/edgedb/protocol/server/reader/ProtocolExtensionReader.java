package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.common.Header;
import edgedb.protocol.common.HeaderReader;
import edgedb.protocol.server.ProtocolExtension;
import edgedb.protocol.server.readerhelper.ReaderHelper;

import java.io.DataInputStream;
import java.io.IOException;

public class ProtocolExtensionReader {
    private DataInputStream dataInputStream;
    private ReaderHelper readerHelper;

    public ProtocolExtensionReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        this.dataInputStream = dataInputStream;
        this.readerHelper = readerHelper;
    }

    public ProtocolExtensionReader(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
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
