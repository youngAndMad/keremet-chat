package danekerscode.keremetchat.utils;

import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JdbcUtilsTest {

    private static final String TEST_TABLE = "keremet_chat_test_table";

    @Test
    void extractTableName_WithoutTableAnnotation_ReturnsValue() {
        var expectedTableName = "jdbc_utils_test";

        var extractTableName = JdbcUtils.extractTableName(this.getClass());

        assertEquals(expectedTableName, extractTableName);
    }

    @Test
    void extractTableName_WithTableAnnotation_ReturnsValue() {
        @Table(name = TEST_TABLE)
        class TestTable {
        }

        var extractTableName = JdbcUtils.extractTableName(TestTable.class);

        assertEquals(TEST_TABLE, extractTableName);
    }
}