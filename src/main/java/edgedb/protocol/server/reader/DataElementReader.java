package edgedb.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.protocol.server.DataElement;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;

@Slf4j
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
                data[i]= readerHelper.getDataInputStream().readByte();
                System.out.print((char)data[i] + " ");
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
