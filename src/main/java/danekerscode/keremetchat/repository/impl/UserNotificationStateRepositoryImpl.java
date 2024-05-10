package danekerscode.keremetchat.repository.impl;

import danekerscode.keremetchat.model.dto.response.IdDto;
import danekerscode.keremetchat.model.entity.UserNotificationState;
import danekerscode.keremetchat.repository.UserNotificationStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserNotificationStateRepositoryImpl implements UserNotificationStateRepository {

    private final JdbcClient jdbc;

    @Override
    public List<UserNotificationState> findAllByUserId(Long id) {
        return jdbc.sql("select * from user_notification_state where user_id=:userId")
                .param("userId", id)
                .query(new BeanPropertyRowMapper<>(UserNotificationState.class))
                .list();
    }

    @Override
    public IdDto<Long> save(Long chatId, Long userId) {
        var keyHolder = new GeneratedKeyHolder();

        jdbc.sql("""
                        insert into user_notification_state(chat_id,user_id, last_received_notification_id)
                        values (:chatId,:userId,0)
                        """)
                .param("userId", userId)
                .param("chatId", chatId)
                .update(keyHolder, "id");

        var id = Objects.requireNonNull(keyHolder.getKey());
        return new IdDto<>(id.longValue());
    }

    @Override
    public void update(Long chatId, Long userId, Long newLastReceivedNotificationId) {
        var affectedRows = jdbc.sql("""
                        update user_notification_state set last_received_notification_id = :newLastReceivedNotificationId
                        where chat_id = :chatId and user_id = :userId
                        """)
                .param("chatId", chatId)
                .param("userId", userId)
                .update();
        log.debug("update of user_notification_state chatId {} userId {} affected rows {}", chatId, userId, affectedRows);
    }
}
