package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.EmailMessageDto;
import danekerscode.keremetchat.service.MailService;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final Configuration ftl;

    @Value("${spring.mail.sender.address}")
    private String sender;

    @Override
    public CompletableFuture<Void> send(EmailMessageDto messageDto) {
        return CompletableFuture.runAsync(() -> {
            var msg = mailSender.createMimeMessage();

            try {
                var helper = new MimeMessageHelper(
                        msg,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name()
                );

                var template = this.ftl.getTemplate(messageDto.type().getTemplateName());

                var html = FreeMarkerTemplateUtils.processTemplateIntoString(template, messageDto.varargs());

                helper.setText(html, true);
                helper.setFrom(sender);
                helper.setTo(messageDto.to());
                helper.setFrom(sender);
                helper.setSubject("Keremet Chat " + messageDto.type().name());

                mailSender.send(msg);

                log.info("Mail sent to {} with type {}", messageDto.to(), messageDto.type());
            } catch (Exception e) {
                log.error("Error while Sending Mail, msg {}", e.getMessage(), e);
            }
        });

    }

}
