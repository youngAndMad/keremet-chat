package danekerscode.keremetchat.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JdbcUtilsTest {

    @Test
    void extractTableName_WithoutTableAnnotation_ReturnsValue() {
        var expectedTableName = "jdbc_utils_test";

        var extractTableName = JdbcUtils.extractTableName(this.getClass());

        assertEquals(expectedTableName, extractTableName);
    }

    @Test
    void extractTableName_WithTableAnnotation_ReturnsValue() {
        final String TEST_TABLE = "keremet_chat_test_table";

        @Table(name = TEST_TABLE)
        class TestTable {
        }

        var extractTableName = JdbcUtils.extractTableName(TestTable.class);

        assertEquals(TEST_TABLE, extractTableName);
    }

    @Test
    void extractIdColumnName_WithCommonIdAnnotation_ReturnsIdColumnName() {
        class TestTable {
            @Id
            Integer testId;
        }

        var actualIdColumnName = JdbcUtils.extractIdColumnName(TestTable.class);

        assertEquals("test_id", actualIdColumnName);
    }

    @Test
    void extractIdColumnName_WithIdAnnotationWithName_ReturnsIdColumnName() {
        final String idColumnName = "id";
        class TestTable {
            @Id
            @Column(name = idColumnName)
            Integer testId;
        }

        var actualIdColumnName = JdbcUtils.extractIdColumnName(TestTable.class);

        assertEquals(idColumnName, actualIdColumnName);
    }

    @Test
    void extractIdColumnName_WithNoIdColumn_ThrowsException() {
        class TestTable {
        }

        assertThrows(IllegalArgumentException.class, () -> JdbcUtils.extractIdColumnName(TestTable.class));
    }
}