package edgedb.protocol.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Header extends BaseClientProtocol {
    short code;
    byte[] value;

    @Override
    public int calculateMessageLength() {
        int messageLength = 0;
        MessageLengthCalculator calculator = new MessageLengthCalculator();

        messageLength += calculator.calculate(code);
        messageLength += calculator.calculate(value);

        return messageLength;
    }
}
