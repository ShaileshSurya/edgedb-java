package edgedb.protocol.constants;

public final class AuthenticationStatus {
    public static final int AUTHENTICATION_OK = 0; //0xa;
    public static final int AUTHENTICATION_SASL = 10; //0xa;
    public static final int AUTHENTICATION_SASL_CONTINUE = 11;//0xb
    public static final int AUTHENTICATION_SASL_FINAL = 12;//0xc
}
