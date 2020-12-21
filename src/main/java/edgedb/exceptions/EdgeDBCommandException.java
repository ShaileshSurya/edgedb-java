package edgedb.exceptions;

import edgedb.internal.protocol.ErrorResponse;
import lombok.Data;

@Data
public class EdgeDBCommandException extends EdgeDBClientException {

    ErrorResponse errorResponse;

    public EdgeDBCommandException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }
}
