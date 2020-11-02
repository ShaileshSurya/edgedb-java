package edgedb.protocol.server.reader;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.exceptions.OverReadException;
import edgedb.protocol.server.BaseServerProtocol;
import edgedb.protocol.server.DataElement;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import edgedb.protocol.typedescriptor.BaseScalarType;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.IOException;

import static edgedb.protocol.constants.IOFormat.BINARY;

@Slf4j
public class DataElementReader extends BaseReader {
    public DataElementReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public DataElementReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    @Override
    public DataElement read() throws IOException {
        DataElement dataElement = new DataElement();

        try {
            int dataElementsLength = readerHelper.readUint32();
            dataElement.setDataLength(dataElementsLength);

//            switch (argumentType){
//                case BINARY:
//                    log.debug("The Argument type was found Binary");
//                    break;
//
//            }

            byte[] data = new byte[dataElementsLength];
            for (int i = 0; i < dataElementsLength; i++) {
                data[i] = readerHelper.getDataInputStream().readByte();
                System.out.print((char) data[i] + " ");
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
