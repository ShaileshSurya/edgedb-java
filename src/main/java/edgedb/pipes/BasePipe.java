package edgedb.pipes;

import edgedb.client.SocketStream;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class BasePipe {
    SocketStream socketStream;
}
