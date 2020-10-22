package com.github.edgedb.protocol.Interface;

import java.io.IOException;

public interface Write {
    public void writeUint8(int value) throws IOException;
    public void writeUint32(int value) throws IOException;
    public void writeUint16(int value) throws IOException;
    public void writeString(String str) throws IOException;
}
