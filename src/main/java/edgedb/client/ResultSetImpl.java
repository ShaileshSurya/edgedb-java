package edgedb.client;

import edgedb.internal.protocol.server.DataResponse;
import lombok.Data;

@Data
public class ResultSetImpl implements ResultSet{

    DataResponse dataResponse;

    @Override
    public void setResultData(DataResponse dataResponse) {
        this.dataResponse= dataResponse;
    }
}
