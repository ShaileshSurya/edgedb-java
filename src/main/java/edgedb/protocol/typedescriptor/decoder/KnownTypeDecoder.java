package edgedb.protocol.typedescriptor.decoder;

import edgedb.exceptions.ScalarTypeNotFoundException;
import edgedb.protocol.typedescriptor.*;

import java.util.Arrays;

public final class KnownTypeDecoder<T extends TypeDescriptor> {

    public final T decode(byte[] value) throws ScalarTypeNotFoundException {

        if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0})) {
            return (T) BaseScalarType.UUID;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1})) {
            return (T) BaseScalarType.STRING;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2})) {
            return (T) BaseScalarType.BYTES;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3})) {
            return (T) BaseScalarType.INT16;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 4})) {
            return (T) BaseScalarType.INT32;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5})) {
            return (T) BaseScalarType.INT64;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 6})) {
            return (T) BaseScalarType.FLOAT32;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 7})) {
            return (T) BaseScalarType.FLOAT64;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 8})) {
            return (T) BaseScalarType.DECIMAL;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 9})) {
            return (T) BaseScalarType.BOOL;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 10})) {
            return (T) BaseScalarType.DATETIME;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 11})) {
            return (T) BaseScalarType.LOCAL_DATE_TIME;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 12})) {
            return (T) BaseScalarType.LOCAL_DATE;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 13})) {
            return (T) BaseScalarType.LOCAL_TIME;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 14})) {
            return (T) BaseScalarType.DURATION;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 15})) {
            return (T) BaseScalarType.JSON;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1})) {
            throw new ScalarTypeNotFoundException();
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1})) {
            return (T) TupleType.ANY_TUPLE;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2})) {
            return (T) TupleType.ANY_TUPLE;
        } else {
            throw new ScalarTypeNotFoundException();
        }
    }
}


