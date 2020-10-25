package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.client.Header;
import edgedb.protocol.server.PrepareComplete;
import edgedb.protocol.server.readerhelper.ReaderHelper;

import java.io.DataInputStream;
import java.io.IOException;

public class PrepareCompleteReader extends BaseReader {
    public PrepareCompleteReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public PrepareCompleteReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public PrepareComplete read() throws IOException {
        PrepareComplete prepareComplete = new PrepareComplete();
        try{
            prepareComplete.setMessageLength(readerHelper.readUint32());

            short headersLength = readerHelper.readUint16();
            prepareComplete.setHeadersLength(headersLength);

            Header[] headers= new Header[headersLength];
            HeaderReader headerReader = new HeaderReader(dataInputStream,readerHelper);
            for(int i=0;i<headersLength;i++){
                headers[i] = headerReader.read();
            }
            prepareComplete.setHeaders(headers);

            prepareComplete.setCardinality(readerHelper.readUint8());

            prepareComplete.setArgumentDataDescriptorID(readerHelper.readUUID());
            prepareComplete.setResultDataDescriptorID(readerHelper.readUUID());

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
