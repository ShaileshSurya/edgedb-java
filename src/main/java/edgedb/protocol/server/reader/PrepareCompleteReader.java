package edgedb.protocol.server.reader;

import edgedb.exceptions.*;
import edgedb.protocol.common.Header;
import edgedb.protocol.common.HeaderReader;
import edgedb.protocol.server.PrepareComplete;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import edgedb.protocol.typedescriptor.decoder.ScalarTypeDecoder;
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

    public PrepareComplete read() throws IOException, FailedToDecodeServerResponseException {
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
            ScalarTypeDecoder decoder = new ScalarTypeDecoder();
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
//        } catch (ScalarTypeNotFoundException e) {
//            e.printStackTrace();
//            throw new FailedToDecodeServerResponseException();
//        }
    }


}
