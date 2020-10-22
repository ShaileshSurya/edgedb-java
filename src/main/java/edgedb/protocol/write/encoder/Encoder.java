package edgedb.protocol.write.encoder;

import java.io.IOException;

public interface Encoder {
    public void encode() throws IOException;
}
