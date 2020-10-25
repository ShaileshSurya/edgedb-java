package edgedb.protocol.client;

import lombok.Data;

@Data
public class Execute extends BaseClientProtocol{
    byte mType = (int) 'E';
    int messageLength;
    short headersLength;
    Header[] headers;
    byte[] statementName;
    byte[] arguments;

    @Override
    public int calculateMessageLength() {
        int length =0;

        MessageLengthCalculator calculator = new MessageLengthCalculator();
        length+= calculator.calculate(messageLength);
        length+= calculator.calculate(headersLength);

        for(int i=0; i< (int)headersLength;i++){
            length+=headers[i].calculateMessageLength();
        }

        length+=calculator.calculate(statementName);
        length+=calculator.calculate(arguments);
        return length;
    }
}
