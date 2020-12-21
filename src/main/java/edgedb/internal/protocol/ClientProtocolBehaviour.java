package edgedb.internal.protocol;

public interface ClientProtocolBehaviour {
    public abstract int calculateMessageLength();
}
