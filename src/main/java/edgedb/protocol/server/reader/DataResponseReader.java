package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.server.DataElement;
import edgedb.protocol.server.DataResponse;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;

@Slf4j
public class DataResponseReader extends BaseReader {
    public DataResponseReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public DataResponseReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public DataResponse read() throws IOException {
        log.debug("Trying to read DataResponse.");

        DataResponse dataResponse = new DataResponse();
        try {
            int messageLength = readerHelper.readUint32();
            dataResponse.setMessageLength(messageLength);

            short dataLength = readerHelper.readUint16();
            dataResponse.setDataLength(dataLength);


            DataElement[] dataElements = new DataElement[dataLength];
            DataElementReader dataElementReader = new DataElementReader(dataInputStream,readerHelper);
            for (int i = 0; i < (int) dataLength; i++) {
                dataElements[i] = dataElementReader.read();
            }

            dataResponse.setDataElements(dataElements);
            return dataResponse;
        } catch (OverReadException e) {
            e.printStackTrace();
            return dataResponse;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
