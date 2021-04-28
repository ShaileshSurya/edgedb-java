package edgedb.exceptions;

import edgedb.exceptions.clientexception.ClientException;
import edgedb.exceptions.clientexception.QueryArgumentException;

import java.util.*;

public interface ErrorCodesToExceptionMap {

    public static final Map<Byte, BaseException> errorCodesMap = initMap();

    public static Map<Byte, BaseException> initMap() {
        Map<Byte, BaseException> map = new HashMap<>();
        map.put((byte)01000000, new ClientException("InternalServerError"));
        map.put((byte)02000000, new ClientException("UnsupportedFeatureError"));
        map.put((byte)04030000, new QueryArgumentException("InvalidReferenceError"));
        return Collections.unmodifiableMap(map);
    }

}
