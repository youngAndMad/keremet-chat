package danekerscode.keremetchat.repository.impl.message;

import danekerscode.keremetchat.model.entity.Message;
import danekerscode.keremetchat.model.modify.message.MessageSaveRequest;
import danekerscode.keremetchat.repository.MessageRepository;
import danekerscode.keremetchat.repository.impl.AbstractJdbcRepository;
import danekerscode.keremetchat.utils.JdbcUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Slf4j
public class JdbcMessageRepository extends AbstractJdbcRepository
        implements MessageRepository {

    @Getter
    private final String tableName;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMessageRepository() {
        this.tableName = JdbcUtils.extractTableName(Message.class);
        this.jdbcInsert = super.createSimpleJdbcInsert();
    }

    @Override
    public Long saveMessage(MessageSaveRequest messageSaveRequest) {
        return jdbcInsert.executeAndReturnKey(
                Map.of(
                        "chat_id", messageSaveRequest.chatId(),
                        "sender_id", messageSaveRequest.senderId(),
                        "content", messageSaveRequest.content(),
                        "sent_at", messageSaveRequest.sentAt(),
                        "parent_id", messageSaveRequest.parentId()
                )).longValue();
    }

}
