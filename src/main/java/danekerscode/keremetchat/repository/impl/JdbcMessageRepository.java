package danekerscode.keremetchat.repository.impl;

import danekerscode.keremetchat.model.entity.Message;
import danekerscode.keremetchat.repository.MessageRepository;
import danekerscode.keremetchat.utils.JdbcUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;

@Repository
public class JdbcMessageRepository implements MessageRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcClient jdbcClient;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMessageRepository(JdbcTemplate jdbcTemplate, JdbcClient jdbcClient) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(JdbcUtils.extractTableName(Message.class))
                .usingGeneratedKeyColumns(JdbcUtils.extractIdColumnName(Message.class));
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Long insertMessage(String content, Long chatId, Long senderId, Long parentId) {
        var data = new HashMap<String, Object>() {{
            put("created_date", LocalDateTime.now());
            put("sent_at", LocalDateTime.now());
            put("content", content);
            put("chat_id", chatId);
            put("sender_id", senderId);
            put("parent_id", parentId);
            put("deleted", false);
            put("edited", false);
        }};

        return jdbcInsert.executeAndReturnKey(data).longValue();
    }

    @Override
    public void insertMessageFile(Long messageId, Long fileEntityId) {
        jdbcTemplate.update("""
                insert into message_files(message_id, file_entity_id)
                values(?,?)
                """, messageId, fileEntityId);
    }

}
