package edgedb.internal.protocol.typedescriptor;

public enum BaseScalarType implements TypeDescriptor {
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
    LOCAL_DATE_TIME,
    LOCAL_DATE,
    LOCAL_TIME,
}

