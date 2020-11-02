package edgedb.pipes;

import edgedb.client.SocketStream;
import edgedb.exceptions.EdgeDBInternalErrException;
import edgedb.protocol.client.writer.BaseWriter;
import edgedb.protocol.server.BaseServerProtocol;
import edgedb.protocol.server.reader.Read;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;

@Data
public abstract class BasePipe {
    protected SocketStream socketStream;

    public BasePipe(SocketStream socketStream) {
        this.socketStream= socketStream;
    }

    public <S extends Read,T extends BaseServerProtocol> T read(S reader) throws IOException, EdgeDBInternalErrException {
        return (T) reader.read();
    }

    public <S extends BaseWriter> void write(S writer) throws IOException {
        writer.write();
    }

    public <S extends BaseWriter> void writeAndFlush(S writer) throws IOException {
        writer.writeAndFlush();
    }
}
