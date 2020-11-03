package edgedb.internal.protocol.server.reader;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.common.HeaderReader;
import edgedb.internal.protocol.server.PrepareComplete;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import edgedb.internal.protocol.typedescriptor.decoder.KnownTypeDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;

@Slf4j
public class PrepareCompleteReader extends BaseReader {
    public PrepareCompleteReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public PrepareCompleteReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public PrepareComplete read() throws IOException, EdgeDBInternalErrException {
        PrepareComplete prepareComplete = new PrepareComplete();
        try {
            prepareComplete.setMessageLength(readerHelper.readUint32());

            short headersLength = readerHelper.readUint16();
            prepareComplete.setHeadersLength(headersLength);

            Header[] headers = new Header[headersLength];
            HeaderReader headerReader = new HeaderReader(dataInputStream, readerHelper);
            for (int i = 0; i < headersLength; i++) {
                headers[i] = headerReader.read();
            }
            prepareComplete.setHeaders(headers);

            prepareComplete.setCardinality(readerHelper.readUint8());

            byte[] argumentDataDescriptorID = readerHelper.readUUID();
            KnownTypeDecoder decoder = new KnownTypeDecoder();
            prepareComplete.setArgumentDataDescriptorID(argumentDataDescriptorID);
            //prepareComplete.setResultDataDescriptor(decoder.decode(argumentDataDescriptorID));

            byte[] resultDataDescriptorID = readerHelper.readUUID();
            prepareComplete.setResultDataDescriptorID(resultDataDescriptorID);
            //prepareComplete.setResultDataDescriptor(decoder.decode(resultDataDescriptorID));

            return prepareComplete;
        } catch (OverReadException e) {
            e.printStackTrace();
            return prepareComplete;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }


}
