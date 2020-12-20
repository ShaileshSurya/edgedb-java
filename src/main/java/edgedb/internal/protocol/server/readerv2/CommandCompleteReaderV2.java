package edgedb.internal.protocol.server.readerv2;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.common.HeaderReader;
import edgedb.internal.protocol.server.BaseServerProtocol;
import edgedb.internal.protocol.server.CommandComplete;
import edgedb.internal.protocol.server.readerhelper.IReaderHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.nio.ByteBuffer;

@Data
@AllArgsConstructor
public class CommandCompleteReaderV2 implements ProtocolReader {

    IReaderHelper readerHelper;

    public CommandComplete read(ByteBuffer readBuffer) throws IOException {

        CommandComplete commandComplete = new CommandComplete();
        try {
            int messageLength = readerHelper.readUint32();
            commandComplete.setMessageLength(messageLength);
            readerHelper.setMessageLength(messageLength);

            short headerLength = readerHelper.readUint16();
            commandComplete.setHeadersLength(headerLength);
            Header[] headers = new Header[headerLength];
            HeaderReader headerReader = new HeaderReader(readerHelper);
            for (int i = 0; i < headerLength; i++) {
                headers[i] = headerReader.read(readBuffer);
            }
            commandComplete.setStatus(readerHelper.readString());
            return commandComplete;
        } catch (OverReadException e) {
            return commandComplete;
        } catch (IOException e) {
            throw e;
        }
    }
}
