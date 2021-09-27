package edgedb.internal.protocol.typedescriptor.decoder;

import edgedb.internal.protocol.typedescriptor.TypeDescriptor;

public interface TypeDecoderFactory {

    public enum Types{
        UUID,
        STRING,
        BYTES,
        INT16,
        INT32,
        INT64,
        FLOAT32,
        FLOAT64,
        DECIMAL,
        BOOL,
        DATETIME,
        DURATION,
        JSON,
        LOCAL_DATETIME,
        LOCAL_DATE,
        BIGINT,
        RELATIVE_DURATION,
        LOCAL_TIME,
        EMPTY_TUPLE,
    }

    public Types getTypeDescriptor(byte[] resultDataDescriptor);
}
