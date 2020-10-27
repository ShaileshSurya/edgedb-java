package edgedb.protocol.constants;

public final class TransactionState {
    public static final char NOT_IN_TRANSACTION = 'I'; //0x49; //73
    public static final char IN_TRANSACTION = 'T'; // 0x54; //84
    public static final char IN_FAILED_TRANSACTION = 'E'; //0x45; //69
}
