package danekerscode.keremetchat.repository.impl;

import danekerscode.keremetchat.common.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@NoRepositoryBean
@RequiredArgsConstructor
public abstract class AbstractJdbcRepository {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected abstract String getTableName();

    protected String getIdColumn() {
        return AppConstants.DEFAULT_ID_COLUMN_NAME.getValue();
    }

    protected SimpleJdbcInsert createSimpleJdbcInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(getTableName())
                .usingGeneratedKeyColumns(getIdColumn());
    }
}
