package danekerscode.keremetchat.repository.impl;

import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import danekerscode.keremetchat.repository.ChatNotificationRepository;
import danekerscode.keremetchat.utils.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Objects;

@Repository
@Slf4j
public class ChatNotificationRepositoryImpl implements ChatNotificationRepository {

    private final JdbcClient jdbc;

    public ChatNotificationRepositoryImpl(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public <Content> IdDto<Long> save(
            Long chatId,
            LocalDateTime notificationTime,
            WebsocketNotificationType type,
            Content content
    ) {
        var keyHolder = new GeneratedKeyHolder();

        jdbc.sql("""
                        insert into chat_notification(created_date, last_modified_date, inner_id, chat_id, notification_time, type, content)
                        values (now(), null,  COALESCE((select max(inner_id) from chat_notification where chat_id=:chatId), 0) + 1,:chatId,:notificationTime, :type, :content)
                        """)
                .param("chatId", chatId)
                .param("notificationTime", notificationTime)
                .param("type", type.name())
                .param("content", ObjectMapperUtils.asString(content), Types.OTHER)
                .update(keyHolder, "id");

        var id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new IdDto<>(id);
    }

    @Override
    public void delete(Long id) {
        var affectedRows = jdbc.sql("delete from chat_notification where id=:id")
                .param("id", id)
                .update();

        log.debug("Delete notification by id {} affected rows {}", id, affectedRows);
    }
}
