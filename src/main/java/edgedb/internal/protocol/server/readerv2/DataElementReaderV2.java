package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.DataElement;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
@AllArgsConstructor
public class DataElementReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    @Override
    public DataElement read(ByteBuffer readBuffer) throws IOException {
        DataElement dataElement = new DataElement();

        try {
            int dataElementsLength = readerHelper.readUint32();
            dataElement.setDataLength(dataElementsLength);

            byte[] data = new byte[dataElementsLength];
            for (int i = 0; i < dataElementsLength; i++) {
                data[i] = readerHelper.readByte();
            }
            dataElement.setDataElement(data);

            return dataElement;
        } catch (OverReadException e) {
            e.printStackTrace();
            return dataElement;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
