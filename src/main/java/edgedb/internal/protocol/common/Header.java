package edgedb.internal.protocol.common;

import edgedb.internal.protocol.client.MessageLengthCalculator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class Header {
    short code;
    byte[] value;

    @Override
    public String toString() {
        return "Header{" +
                "code=" + code +
                ", value=" + new String(value) +
                '}';
    }

    public int calculateMessageLength() {
        int messageLength = 0;
        MessageLengthCalculator calculator = new MessageLengthCalculator();

        messageLength += calculator.calculate(code);
        messageLength += calculator.calculate(value);

        return messageLength;
    }
}
