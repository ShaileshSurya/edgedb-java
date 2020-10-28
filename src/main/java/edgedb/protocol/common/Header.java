package edgedb.protocol.common;

import edgedb.protocol.client.BaseClientProtocol;
import edgedb.protocol.client.MessageLengthCalculator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Header {
    short code;
    byte[] value;


    public int calculateMessageLength() {
        int messageLength = 0;
        MessageLengthCalculator calculator = new MessageLengthCalculator();

        messageLength += calculator.calculate(code);
        messageLength += calculator.calculate(value);

        return messageLength;
    }
}
