package edgedb.protocol;



public class BaseProtocol {
    byte mType;
    int messageLength;
    short majorVersion;
    short minorVersion;

    public int calculateMessageLength(){
        return Byte.SIZE / Byte.SIZE +
                Integer.SIZE /Byte.SIZE +
                Short.SIZE / Byte.SIZE +
                Short.SIZE / Byte.SIZE ;
    }
}
