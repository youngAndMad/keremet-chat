package danekerscode.keremetchat.authserver.client.feign;

import danekerscode.keremetchat.authserver.model.dto.EmailMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.concurrent.CompletableFuture;

@FeignClient(name = "keremet-chat-mail-sender")
public interface MailSenderClient {

    @PostMapping(value = "/send/mail" , consumes = "application/multipart-form-data")
    CompletableFuture<ResponseEntity<String>> sendEmail(EmailMessageDto emailMessageDto);

}
