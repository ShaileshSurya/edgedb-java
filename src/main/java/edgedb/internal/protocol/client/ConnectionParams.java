package edgedb.internal.protocol.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionParams extends BaseClientProtocol {
    String name;
    String value;

    @Override
    public int calculateMessageLength() {
        MessageLengthCalculator calculator = new MessageLengthCalculator();
        int length = 0;
        length += calculator.calculate(name);
        length += calculator.calculate(value);
        return length;
    }

}
