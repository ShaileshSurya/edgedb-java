package edgedb.internal.protocol.server.readerv2;

import edgedb.internal.protocol.Header;
import edgedb.internal.protocol.PrepareComplete;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import edgedb.internal.protocol.typedescriptor.decoder.KnownTypeDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
@AllArgsConstructor
public class PrepareCompleteReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    public PrepareComplete read(ByteBuffer buffer) throws IOException {
        PrepareComplete prepareComplete = new PrepareComplete();

        prepareComplete.setMessageLength(readerHelper.readUint32());

        short headersLength = readerHelper.readUint16();
        prepareComplete.setHeadersLength(headersLength);

        Header[] headers = new Header[headersLength];
        ProtocolReader headerReader = new HeaderReader(readerHelper);
        for (int i = 0; i < headersLength; i++) {
            headers[i] = headerReader.read(buffer);
        }
        prepareComplete.setHeaders(headers);

        prepareComplete.setCardinality(readerHelper.readUint8());

        byte[] argumentDataDescriptorID = readerHelper.readUUID();
        KnownTypeDecoder decoder = new KnownTypeDecoder();
        prepareComplete.setArgumentDataDescriptorID(argumentDataDescriptorID);

        byte[] resultDataDescriptorID = readerHelper.readUUID();
        prepareComplete.setResultDataDescriptorID(resultDataDescriptorID);

        return prepareComplete;
    }


}
