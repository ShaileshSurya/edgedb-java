package edgedb.exceptions;

import lombok.Data;

@Data
public class EdgeDBSocketException extends EdgeDBException{
    private String host;
    private int port;

    public EdgeDBSocketException(Throwable e, String host, int port){
        super(e);
        this.host= host;
        this.port = port;
    }
}
