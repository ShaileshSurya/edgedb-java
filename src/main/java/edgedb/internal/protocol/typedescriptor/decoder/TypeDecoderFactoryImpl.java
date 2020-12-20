package edgedb.internal.protocol.typedescriptor.decoder;

import edgedb.internal.protocol.typedescriptor.TypeDescriptor;

public class TypeDecoderFactoryImpl implements TypeDecoderFactory {
    @Override
    public TypeDescriptor getTypeDescriptor(byte[] resultDataDescriptor) throws TypeNotPresentException {
       throw new UnsupportedOperationException();
    }
}
