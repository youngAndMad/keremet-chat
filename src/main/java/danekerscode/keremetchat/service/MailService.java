package danekerscode.keremetchat.service;

import danekerscode.keremetchat.model.dto.SendMailArgs;

import java.util.concurrent.CompletableFuture;

public interface MailService {

    CompletableFuture<Void> sendMail(SendMailArgs args);

}
