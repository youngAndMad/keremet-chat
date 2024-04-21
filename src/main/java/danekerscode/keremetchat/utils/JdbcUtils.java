package danekerscode.keremetchat.utils;

import jakarta.persistence.Table;

public class JdbcUtils {

    public static String extractTableName(
            Class<?> clazz
    ) {
        var table = clazz.getAnnotation(Table.class);
        return table == null ? toSnakeCase(clazz.getSimpleName()) : table.name();
    }

    private static String toSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
}
