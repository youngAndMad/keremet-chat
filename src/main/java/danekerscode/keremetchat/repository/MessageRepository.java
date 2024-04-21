package danekerscode.keremetchat.repository;

import danekerscode.keremetchat.model.modify.message.MessageSaveRequest;

public interface MessageRepository{

    Long saveMessage(MessageSaveRequest messageSaveRequest);

}