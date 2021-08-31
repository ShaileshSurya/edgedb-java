package edgedb.internal.protocol.server.readerv2;

import edgedb.internal.protocol.DataElement;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
@AllArgsConstructor
public class DataElementReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    @Override
    public DataElement read(ByteBuffer readBuffer) {
        DataElement dataElement = new DataElement();
        
        int dataElementsLength = readerHelper.readUint32();
        dataElement.setDataLength(dataElementsLength);

        byte[] data = new byte[dataElementsLength];
        for (int i = 0; i < dataElementsLength; i++) {
            data[i] = readerHelper.readByte();
        }
        dataElement.setDataElement(data);

        return dataElement;

    }
}
