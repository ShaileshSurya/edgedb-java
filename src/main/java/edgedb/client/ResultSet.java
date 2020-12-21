package edgedb.client;

import edgedb.internal.protocol.DataResponse;

public interface ResultSet {
    public void setResultData(DataResponse dataResponse);
}
