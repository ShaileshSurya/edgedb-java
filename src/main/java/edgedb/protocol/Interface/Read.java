package com.github.edgedb.protocol.Interface;

import java.io.IOException;

public interface Read{
    public byte readUint8() throws IOException;
    public int readUint32() throws IOException;
    public short readUint16() throws IOException;
    public String readString() throws IOException;
}
