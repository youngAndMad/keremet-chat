package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.CreatePrivateChatRequest;
import danekerscode.keremetchat.model.dto.response.IdDto;

import java.util.List;

public interface ChatService {

    IdDto<Long> createChat(CreatePrivateChatRequest createChatRequest);

    void deletePrivateChat(Long chatId);

    List<Long> getMemberUsersIdList(Long chatId);

    boolean existsById(Long chatId);
}
