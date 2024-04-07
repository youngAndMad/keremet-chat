package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.EmailMessageDto;

import java.util.concurrent.CompletableFuture;

public interface MailService {

    CompletableFuture<Void> send(EmailMessageDto messageDto);

}
