package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.server.DataElement;
import edgedb.protocol.server.readerhelper.ReaderHelper;

import java.io.DataInputStream;
import java.io.IOException;

public class DataElementReader extends BaseReader {
    public DataElementReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public DataElementReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public DataElement read() throws IOException {
        DataElement dataElement = new DataElement();

        try{
            int dataElementsLength = readerHelper.readUint32();
            dataElement.setDataLength(dataElementsLength);

            byte[] data = new byte[dataElementsLength];

            for(int i=0;i<dataElementsLength;i++){
                data[i]= readerHelper.readUint8();
            }
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
