package edgedb.protocol.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionParams extends BaseClientProtocol {
    String name;
    String value;

    @Override
    public int calculateMessageLength() {
        int length = 0;
        return length;
    }
}
