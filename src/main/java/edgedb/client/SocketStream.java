package edgedb.client;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.DataInputStream;
import java.io.DataOutputStream;

@AllArgsConstructor
@Data
public class SocketStream {
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
}
