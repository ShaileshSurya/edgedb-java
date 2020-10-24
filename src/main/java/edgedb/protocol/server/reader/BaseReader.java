package edgedb.protocol.server.reader;

import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataInputStream;

@Data
@AllArgsConstructor
abstract class BaseReader {
    DataInputStream dataInputStream;
    ReaderHelper readerHelper;

    private BaseReader(ReaderHelper readerHelper) {
    }

    public BaseReader(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
        this.readerHelper = new ReaderHelper(dataInputStream);
    }

}
