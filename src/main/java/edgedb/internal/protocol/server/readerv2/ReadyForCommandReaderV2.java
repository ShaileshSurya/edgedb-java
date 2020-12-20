package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.common.HeaderReader;
import edgedb.internal.protocol.server.ReadyForCommand;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import lombok.AllArgsConstructor;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@AllArgsConstructor
public class ReadyForCommandReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    public ReadyForCommand read(ByteBuffer buffer) throws IOException {
        ReadyForCommand readyForCommand = new ReadyForCommand();

        try {
            readyForCommand.setMessageLength(readerHelper.readUint32());

            short headersLength = readerHelper.readUint16();
            readyForCommand.setHeadersLength(headersLength);

            Header[] headers = new Header[headersLength];
            ProtocolReader headerReader = new HeaderReader(readerHelper);
            for (int i = 0; i < headersLength; i++) {
                headers[i] = headerReader.read(buffer);
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
}
