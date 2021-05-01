package edgedb.exceptions.constants;

import java.util.*;

public final class Severity {

    public static final String ERROR = "ERROR";
    public static final String FATAL = "FATAL";
    public static final String PANIC = "PANIC";

    public static final Map<Short, String> severityMap = initMap();

    public static Map<Short, String> initMap() {
        Map<Short, String> map = new HashMap<>();
        map.put((short) 0x78, ERROR);
        map.put((short) 0xc8, FATAL);
        map.put((short) 0xff, PANIC);

        return Collections.unmodifiableMap(map);
    }

}
