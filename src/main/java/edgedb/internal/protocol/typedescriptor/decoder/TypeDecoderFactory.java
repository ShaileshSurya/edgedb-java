package edgedb.internal.protocol.typedescriptor.decoder;

import edgedb.internal.protocol.typedescriptor.TypeDescriptor;

public interface TypeDecoderFactory {
    public TypeDescriptor getTypeDescriptor(byte[] resultDataDescriptor);
}
