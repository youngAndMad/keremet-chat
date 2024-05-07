package danekerscode.keremetchat.core.mapper;

import danekerscode.keremetchat.model.dto.request.websocket.MessageRequest;
import danekerscode.keremetchat.model.entity.FileEntity;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.notification.MessageNotification;
import danekerscode.keremetchat.model.projection.ChatProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(uses = {UserMapper.class}, imports = {LocalDateTime.class})
public interface MessageMapper {

    @Mapping(target = "chatName", expression = "java(chat.getName())")
    @Mapping(target = "chatId", expression = "java(chat.getId())")
    @Mapping(target = "parentId", source = "messageRequest.parentId")
    @Mapping(target = "content", source = "messageRequest.content")
    @Mapping(target = "files", expression = "java(files)")
    @Mapping(target = "sentTime", constant = "LocalDateTime.now()")
    MessageNotification toNotification(
            User sender,
            ChatProjection chat,
            MessageRequest messageRequest,
            List<FileEntity> files
    );
}
