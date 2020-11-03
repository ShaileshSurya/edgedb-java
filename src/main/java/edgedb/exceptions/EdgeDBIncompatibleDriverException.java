package edgedb.exceptions;

import lombok.Data;

@Data
public class EdgeDBIncompatibleDriverException extends EdgeDBException {
    int serverMajorVersion;
    int serverMinorVersion;

    int clientMajorVersion;
    int clientServerVersion;

    public EdgeDBIncompatibleDriverException(String message, int serverMajorVersion, int serverMinorVersion) {
        super(message);
        this.serverMajorVersion = serverMajorVersion;
        this.serverMinorVersion = serverMinorVersion;
    }
}
