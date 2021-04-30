package edgedb.connectionparams;

import lombok.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionParams {

    private String dsn;
    private String host;
    private int port;
    private String admin;
    private String user;
    private String password ;
    private String database;
    private int timeout;

//    public ConnectionParams(String dsn) {
//        URI uri = URI.create(dsn);
//        user = uri.getUserInfo();
//        host = uri.getHost();
//        port = uri.getPort();
//    }
}
