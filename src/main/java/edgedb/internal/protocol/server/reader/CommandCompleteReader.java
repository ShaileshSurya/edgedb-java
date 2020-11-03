package edgedb.internal.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.common.HeaderReader;
import edgedb.internal.protocol.server.CommandComplete;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;

@Data
public class CommandCompleteReader extends BaseReader {

    public CommandCompleteReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public CommandCompleteReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    public CommandComplete read() throws IOException {

        CommandComplete commandComplete = new CommandComplete();
        try {
            int messageLength = readerHelper.readUint32();
            commandComplete.setMessageLength(messageLength);
            readerHelper.setMessageLength(messageLength);

            short headerLength = readerHelper.readUint16();
            commandComplete.setHeadersLength(headerLength);
            Header[] headers = new Header[headerLength];
            HeaderReader headerReader = new HeaderReader(dataInputStream, this.readerHelper);
            for (int i = 0; i < headerLength; i++) {
                headers[i] = headerReader.read();
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
