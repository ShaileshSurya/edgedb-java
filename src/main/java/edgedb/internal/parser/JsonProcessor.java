package edgedb.internal.parser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.IOException;
import java.util.List;

public class JsonProcessor { // This implementation can change as you want.

    public static <T> List<T> unmarshalToList(String json, Class<T> classType)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionType javaType = mapper.getTypeFactory() // Check here whether you can take different function to directly de-serialize to the object than to a List.
                .constructCollectionType(List.class, classType);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(json, javaType);
    }

    public static <T> T unmarshalToType(String json, Class<T> classType)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory()
                .constructType(classType);
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper.readValue(json, javaType);
    }
}
