package edgedb.protocol.typedescriptor.decoder;

import edgedb.exceptions.ScalarTypeNotFoundException;
import edgedb.protocol.typedescriptor.BaseScalarType;

import java.util.Arrays;

public final class ScalarTypeDecoder {

    public static final BaseScalarType decode(byte[] value) throws ScalarTypeNotFoundException {

        if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0})) {
            return BaseScalarType.UUID;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1})) {
            return BaseScalarType.STRING;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2})) {
            return BaseScalarType.BYTES;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3})) {
            return BaseScalarType.INT16;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 4})) {
            return BaseScalarType.INT32;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 5})) {
            return BaseScalarType.INT64;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 6})) {
            return BaseScalarType.FLOAT32;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 7})) {
            return BaseScalarType.FLOAT64;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 8})) {
            return BaseScalarType.DECIMAL;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 9})) {
            return BaseScalarType.BOOL;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 10})) {
            return BaseScalarType.DATETIME;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 11})) {
            return BaseScalarType.LOCAL_DATE_TIME;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 12})) {
            return BaseScalarType.LOCAL_DATE;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 13})) {
            return BaseScalarType.LOCAL_TIME;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 14})) {
            return BaseScalarType.DURATION;
        } else if (Arrays.equals(value, new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 15})) {
            return BaseScalarType.JSON;
        } else {
            throw new ScalarTypeNotFoundException();
        }
    }
}


