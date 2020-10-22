package edgedb.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import lombok.Data;
@Data
@Slf4j
@AllArgsConstructor
public class EdgeDB {
    Connection connection;

    public EdgeDB connect(String dsn) throws Exception{
        log.debug("Connection to edgedb started with dsn {}",dsn);
        connection = new Connection(dsn);
        connection.connect();
        return this;
    }
}
