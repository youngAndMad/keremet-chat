package danekerscode.keremetchat.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class ObjectMapperUtils {
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String writeMap(Map<String, Object> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static Map<String, Object> parseMap(String data) {
        try {
            return objectMapper.readValue(data, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public static Map<String,Object> parseMap(Object o){
        if (o == null) {
            return null;
        }

        var mapType = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        return objectMapper.convertValue(o, mapType);
    }

    public static String asString(Object o){
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
