package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.server.DataElement;
import edgedb.internal.protocol.server.DataResponse;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
@AllArgsConstructor
public class DataResponseReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    public DataResponse read(ByteBuffer buffer) throws IOException {
        log.debug("Trying to read DataResponse.");

        DataResponse dataResponse = new DataResponse();
        try {
            int messageLength = readerHelper.readUint32();
            dataResponse.setMessageLength(messageLength);

            short dataLength = readerHelper.readUint16();
            dataResponse.setDataLength(dataLength);


            DataElement[] dataElements = new DataElement[dataLength];
            DataElementReaderV2 dataElementReader = new DataElementReaderV2(readerHelper);
            for (int i = 0; i < (int) dataLength; i++) {
                dataElements[i] = dataElementReader.read(buffer);
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
