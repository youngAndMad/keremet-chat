package danekerscode.keremetchat.common.mapper;

import danekerscode.keremetchat.model.dto.request.websocket.MessageRequest;
import danekerscode.keremetchat.model.entity.User;
import danekerscode.keremetchat.model.modify.message.MessageSaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Mapping(target = "chatId", expression = "java(chatId)")
    @Mapping(target = "senderId", expression = "java(sender.getId())")
    MessageSaveRequest toSaveRequest(
            MessageRequest messageRequest,
            Long chatId,
            User sender,
            List<Long> savedFileIdList
    );

}
