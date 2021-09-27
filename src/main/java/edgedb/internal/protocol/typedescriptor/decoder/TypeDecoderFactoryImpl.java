package edgedb.internal.protocol.typedescriptor.decoder;

import java.util.HashMap;
import java.util.Map;

public class TypeDecoderFactoryImpl implements TypeDecoderFactory {

    Map<byte[], Types> codec = initMap();

    static Map<byte[], Types> initMap() {
        Map<byte[], Types> codec = new HashMap();

        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x00}, (Types.UUID));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x01}, (Types.STRING));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x02}, (Types.BYTES));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x00}, (Types.INT16));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x04}, (Types.INT32));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x05}, (Types.INT64));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x06}, (Types.FLOAT32));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x07}, (Types.FLOAT64));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x08}, (Types.DECIMAL));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x09}, (Types.BOOL));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x0A}, (Types.DATETIME));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x0E}, (Types.DURATION));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x0F}, (Types.JSON));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x0B}, (Types.LOCAL_DATETIME));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x0C}, (Types.LOCAL_DATE));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x0D}, (Types.LOCAL_TIME));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x10}, (Types.BIGINT));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x1, (byte) 0x11}, (Types.RELATIVE_DURATION));
        codec.put(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0xFF}, (Types.EMPTY_TUPLE));

        return codec;
    }

    @Override
    public Types getTypeDescriptor(byte[] resultDataDescriptor) throws TypeNotPresentException {

        if (codec.containsKey(resultDataDescriptor)) {
            return codec.get(resultDataDescriptor);
        } else {
            throw new TypeNotPresentException("Type not found", new Throwable());
        }
    }


}
