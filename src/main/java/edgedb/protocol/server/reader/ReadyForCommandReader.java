package edgedb.protocol.server.reader;

import edgedb.exceptions.FailedToDecodeServerResponseException;
import edgedb.exceptions.OverReadException;
import edgedb.protocol.common.Header;
import edgedb.protocol.common.HeaderReader;
import edgedb.protocol.server.ReadyForCommand;
import edgedb.protocol.server.readerhelper.ReaderHelper;

import java.io.DataInputStream;
import java.io.IOException;

import static edgedb.protocol.constants.TransactionState.*;

public class ReadyForCommandReader extends BaseReader{


    public ReadyForCommandReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public ReadyForCommandReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public ReadyForCommand read() throws IOException, FailedToDecodeServerResponseException {
        ReadyForCommand readyForCommand = new ReadyForCommand();

        try{
            readyForCommand.setMessageLength(readerHelper.readUint32());

            short headersLength = readerHelper.readUint16();
            readyForCommand.setHeadersLength(headersLength);

            Header[] headers= new Header[headersLength];
            HeaderReader headerReader = new HeaderReader(dataInputStream,readerHelper);
            for(int i=0;i<headersLength;i++){
                headers[i] = headerReader.read();
            }
            readyForCommand.setHeaders(headers);

            readyForCommand.setTransactionState(readerHelper.readUint8());

            return readyForCommand;

        } catch (OverReadException e) {
            e.printStackTrace();
            return readyForCommand;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public String decodeTransactionState(short state) throws FailedToDecodeServerResponseException {

        switch (state) {
            case (int) IN_TRANSACTION:
                return "IN_TRANSACTION";
            case (int) IN_FAILED_TRANSACTION:
                return "IN_FAILED_TRANSACTION";
            case (int) NOT_IN_TRANSACTION:
                return "NOT_IN_TRANSACTION";
            default:
                throw new FailedToDecodeServerResponseException();
        }
    }
}
