package edgedb.protocol.server.reader;

import edgedb.protocol.server.readerhelper.ReaderHelper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataInputStream;

@Data
@AllArgsConstructor
public abstract class BaseReader implements Read{
    DataInputStream dataInputStream;
    ReaderHelper readerHelper;

    private BaseReader(ReaderHelper readerHelper) {
    }

    public BaseReader(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
        this.readerHelper = new ReaderHelper(dataInputStream);
    }


}
