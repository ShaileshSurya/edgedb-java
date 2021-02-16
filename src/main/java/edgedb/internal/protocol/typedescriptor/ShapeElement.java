package edgedb.internal.protocol.typedescriptor;

import lombok.Data;

@Data
public class ShapeElement {
    // Field flags:
    //   1 << 0: the field is implicit
    //   1 << 1: the field is a link property
    //   1 << 2: the field is a link
    private char flags;

    // Field name.
    private String name;

    // Field type descriptor index.
    private short type_pos;
}
