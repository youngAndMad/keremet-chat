package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.notification.ChatNotification;
import danekerscode.keremetchat.model.notification.CommonChatNotificationRequest;
import danekerscode.keremetchat.service.ChatNotificationService;
import danekerscode.keremetchat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatNotificationServiceImpl implements ChatNotificationService {

    private final JdbcClient jdbc;
    private final ChatService chatService;

    @Override
    @Transactional
    public ChatNotification saveCommonNotification(
            CommonChatNotificationRequest request,
            UserResponseDto currentUser,
            Long chatId
    ) {
        var chat = chatService.findById(chatId);

        var member = chat.memberForUser(currentUser.id());
        log.info("saveCommonNotification for member {}", member.getId());

        var chatNotification = new ChatNotification();
        chatNotification.setSenderId(currentUser.id());
        chatNotification.setChatId(chatId);
        chatNotification.setType(request.type());

        var idKeyHolder = new GeneratedKeyHolder();
        var now = LocalDateTime.now();
        jdbc.sql("""
                        insert into chat_notification(created_date, inner_id, chat_id, notification_time, type, content)
                        values (
                        :createdTime,:innerId,
                        :chatId, :notificationTime, :type, :content);
                        """)
                .param("createdTime", now)
                .param("chatId", chatId)
                .param("innerId", this.lastNotificationInnerId(chatId))
                .param("notificationTime", now)
                .param("type", request.type().name())
                .param("content", null)
                .update(idKeyHolder, "id"); //todo: move to repository layer

        var id = Objects.requireNonNull(idKeyHolder.getKey()).longValue();
        chatNotification.setId(id);
        return chatNotification;
    }

    @Override
    public void cascadeForChat(Long chatId) {
        jdbc.sql("delete from chat_notification where chat_id = :chatId")
                .param("chatId", chatId)
                .update();
    }

    @Override
    public Long lastNotificationInnerId(Long chatId) {
        return (Long) jdbc.sql("select COALESCE((select max(inner_id) from chat_notification where chat_id=:chatId), 0) + 1")
                .param("chatId", chatId)
                .query()
                .singleValue();
    }
}
