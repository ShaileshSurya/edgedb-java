package edgedb.internal.protocol.server.reader;

import edgedb.exceptions.OverReadException;
import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.common.HeaderReader;
import edgedb.internal.protocol.server.BaseServerProtocol;
import edgedb.internal.protocol.server.CommandComplete;
import edgedb.internal.protocol.server.readerhelper.ReaderHelper;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@Data
public class CommandCompleteReader extends BaseReader implements ProtocolReader {

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

    @Override
    public <T extends BaseServerProtocol> T Read(ByteBuffer readBuffer) {
        Read helper = (Read) new ReaderHelper(dataInputStream);
        CommandComplete commandComplete = new CommandComplete();
        commandComplete.setMessageLength(commandComplete.getMessageLength());
        return null;
    }
}
