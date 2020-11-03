package edgedb.internal.protocol.server;

import edgedb.internal.protocol.common.Header;
import edgedb.internal.protocol.typedescriptor.BaseScalarType;
import lombok.Data;

import static edgedb.internal.protocol.constants.MessageType.PREPARE_COMPLETE;

@Data
public class PrepareComplete extends BaseServerProtocol {
    byte mType = (int) PREPARE_COMPLETE;
    int messageLength;

    short headersLength;
    Header[] headers;

    byte cardinality;

    byte[] argumentDataDescriptorID;
    // decoded argumentDataDescriptorID in String format
    BaseScalarType argumentDataDiscriptor;

    byte[] resultDataDescriptorID;
    // decoded resultDataDescriptorID in String format
    BaseScalarType resultDataDescriptor;


}
