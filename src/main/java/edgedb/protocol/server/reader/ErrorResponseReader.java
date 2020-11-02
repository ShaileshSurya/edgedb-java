package edgedb.protocol.server.reader;

import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.protocol.server.BaseServerProtocol;
import edgedb.protocol.server.ErrorResponse;
import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.Data;

import java.io.DataInputStream;
import java.io.IOException;

@Data
public class ErrorResponseReader extends BaseReader {
    ErrorResponse errorResponse;

    public ErrorResponseReader(DataInputStream dataInputStream, ReaderHelper readerHelper) {
        super(dataInputStream, readerHelper);
    }

    public ErrorResponseReader(DataInputStream dataInputStream) {
        super(dataInputStream);
    }

    @Override
    public BaseServerProtocol read() throws IOException, EdgeDBInternalErrException {
        return null;
    }
}
