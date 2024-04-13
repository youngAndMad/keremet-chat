package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.model.dto.EmailMessageDto;
import danekerscode.keremetchat.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
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
                enrichMimeMessageForHtml(messageDto, msg);

                var start = System.currentTimeMillis();
                mailSender.send(msg);
                var total = System.currentTimeMillis() - start;
                log.info("Mail sent to {} with type {} time: {}ms", messageDto.to(), messageDto.type(), total);
            } catch (Exception e) {
                log.error("Error while Sending Mail, msg {}", e.getMessage(), e);
            }
        });

    }

    private void enrichMimeMessageForHtml(EmailMessageDto messageDto, MimeMessage msg)
            throws MessagingException, IOException, TemplateException {
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
    }

}
