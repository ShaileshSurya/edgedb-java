package edgedb.connectionparams;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionParams {

    private String dsn;
    @NonNull private String host;
    @NonNull  private int port;
    private String admin;
    @NonNull private String user;
    @NonNull private String password ;
    private String database;
    private int timeout;

//    public ConnectionParams(String dsn) {
//        URI uri = URI.create(dsn);
//        user = uri.getUserInfo();
//        host = uri.getHost();
//        port = uri.getPort();
//    }
}
