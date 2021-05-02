package edgedb.client;

public interface SingleResultCallBack<T> {
    public void onResult(final Void result, final Throwable t);
}
