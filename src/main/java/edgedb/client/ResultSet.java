package edgedb.client;

import edgedb.internal.protocol.server.DataResponse;

public interface ResultSet {
    public void setResultData(DataResponse dataResponse);
}
