package danekerscode.keremetchat.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMapperUtilsTest {

    @Test
    void parseMap_invalidData_throwsException() {
        var nonJsonString = "non-json-string";

        assertThrows(IllegalArgumentException.class, () -> ObjectMapperUtils.parseMap(nonJsonString));
    }

    @Test
    void parseMap_validData_returnsMap() {
        var jsonString = "{\"key\": \"value\"}";

        var parseMap = ObjectMapperUtils.parseMap(jsonString);

        assertEquals(1, parseMap.size());
        assertEquals("value", parseMap.get("key"));
    }

    @Test
    void writeMap_invalidData_throwsException() {
        var invalidData = Map.of("key", new Object());

        assertThrows(IllegalArgumentException.class, () -> ObjectMapperUtils.writeMap(invalidData));
    }

    @Test
    void writeMap_validData_returnsJsonString() {
        var validData = new HashMap<String,Object>(Map.of("key", "value"));

        var writeMap = ObjectMapperUtils.writeMap(validData);

        assertEquals("{\"key\":\"value\"}", writeMap);
    }
}