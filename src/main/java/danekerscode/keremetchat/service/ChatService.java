package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.request.CreatePrivateChatRequest;
import danekerscode.keremetchat.model.dto.response.IdDto;

public interface ChatService {
    IdDto<Long> createChat(CreatePrivateChatRequest createChatRequest);

}
