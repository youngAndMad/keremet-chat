package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.KeyPair;
import danekerscode.keremetchat.model.dto.response.UserResponseDto;
import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import danekerscode.keremetchat.model.exception.EntityNotFoundException;
import danekerscode.keremetchat.model.notification.ChatNotification;
import danekerscode.keremetchat.model.notification.CommonChatNotificationRequest;
import danekerscode.keremetchat.model.notification.CommonStopChatNotificationRequest;
import danekerscode.keremetchat.service.ChatNotificationService;
import danekerscode.keremetchat.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class ChatNotificationServiceImpl implements ChatNotificationService {
    // todo move jdbc logic to repository layer
    private final JdbcClient jdbc;
    private final ChatService chatService;

    public ChatNotificationServiceImpl(JdbcClient jdbc, @Lazy ChatService chatService) {
        this.jdbc = jdbc;
        this.chatService = chatService;
    }

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
                        insert into chat_notification(created_date, inner_id, chat_id, notification_time, type, content,sender_id)
                        values (
                        :createdTime,:innerId,
                        :chatId, :notificationTime, :type, :content, :senderId);
                        """)
                .param("createdTime", now)
                .param("chatId", chatId)
                .param("innerId", this.lastNotificationInnerId(chatId))
                .param("notificationTime", now)
                .param("type", request.type().name())
                .param("content", null)
                .param("senderId", member.getId())
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

    @Override
    public void deleteNotification(
            CommonStopChatNotificationRequest request,
            UserResponseDto currentUser,
            Long chatId
    ) {
        var chat = chatService.findById(chatId);
        var currentMember = chat.memberForUser(currentUser.id());

        var affectedRows = jdbc.sql("""
                        delete from chat_notification
                        where chat_id = :chatId
                        and sender_id =: senderId
                        and type =: type
                        """)
                .param("chatId", chatId)
                .param("senderId", currentMember.getId())
                .param("type", request.type().name())
                .update();

        log.debug("Deleted notifications by sender user {}, chatId {} type {} affected rows {}",
                currentUser.email(),
                chat.getId(),
                request.type(),
                affectedRows
        );
    }

    @Override
    public ChatNotification findBySenderAndType(Long senderId, WebsocketNotificationType type) {
        return jdbc.sql("""
                        select * from chat_notification
                        where type =: type
                        and sender_id =: senderId
                        """)
                .param("senderId", senderId)
                .param("type", type)
                .query(new BeanPropertyRowMapper<>(ChatNotification.class))
                .optional()
                .orElseThrow(() -> new EntityNotFoundException(ChatNotification.class, KeyPair.of("senderId", senderId), KeyPair.of("type", type)));
    }
}
