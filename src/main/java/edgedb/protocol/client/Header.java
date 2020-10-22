package edgedb.protocol.client;

import lombok.Data;

@Data
public class Header {
    short code;
    byte[] value;
}
