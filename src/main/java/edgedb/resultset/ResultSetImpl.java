package edgedb.resultset;

import edgedb.client.ResultSet;
import edgedb.internal.protocol.DataResponse;
import lombok.Data;

import javax.xml.bind.Element;
import java.util.ArrayList;
import java.util.List;

@Data
public class ResultSetImpl implements ResultSet {
    List<DataResponse> dataResponses = new ArrayList<>();
    @Override
    public void setResultData(DataResponse dataResponse) {
        dataResponses.add(dataResponse);
    }
}
