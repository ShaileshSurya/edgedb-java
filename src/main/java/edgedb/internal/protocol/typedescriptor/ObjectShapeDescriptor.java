package edgedb.internal.protocol.typedescriptor;

import lombok.Data;

@Data
public class ObjectShapeDescriptor {
    private char type = 1;
    private byte[] id;
    private short elementCount;
    private ShapeElement[] shapeElements;
}
