package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.common.HeaderReader;
import edgedb.internal.protocol.ProtocolBehaviourExtension;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;

@AllArgsConstructor
public class ProtocolExtensionReaderV2 implements ProtocolReader {

    private IReaderHelper readerHelper;

    public ProtocolBehaviourExtension read(ByteBuffer buffer) throws IOException {
        ProtocolBehaviourExtension protocolExtension = new ProtocolBehaviourExtension();

        try {
            protocolExtension.setName(readerHelper.readString());

            // This is violating DRY.
            short headersLength = readerHelper.readUint16();
            protocolExtension.setHeadersLength(headersLength);
            Header[] headers = new Header[headersLength];
            HeaderReader headerReader = new HeaderReader(readerHelper);
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
