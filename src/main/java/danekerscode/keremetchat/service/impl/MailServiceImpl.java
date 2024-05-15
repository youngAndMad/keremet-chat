package danekerscode.keremetchat.service.impl;

import danekerscode.keremetchat.config.properties.AppProperties;
import danekerscode.keremetchat.model.dto.SendMailArgs;
import danekerscode.keremetchat.service.MailService;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final AppProperties appProperties;
    private final JavaMailSender mailSender;
    private final Configuration ftl;

    @Override
    public CompletableFuture<Void> sendMail(SendMailArgs args) {
        return CompletableFuture.runAsync(() -> {
            var msg = mailSender.createMimeMessage();
            try {
                var helper = new MimeMessageHelper(
                        msg,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name()
                );

                Assert.notNull(args.getType(), "Type of mail message can not be null");

                var template = ftl.getTemplate(args.getType().getTemplateName());
                var html = FreeMarkerTemplateUtils.processTemplateIntoString(template, new HashMap<>(args.getProperties()));

                helper.setText(html, true);
                helper.setTo(args.getReceiverEmail());
                helper.setFrom(appProperties.getMail().getSender());
                helper.setSubject(args.getType().getSubject());

                mailSender.send(msg);

                log.info("Successfully delivered mail message. Receiver: {}, Type: {}",
                        args.getReceiverEmail(),
                        args.getType()
                );
            } catch (Exception e) {
                log.error("Error while Sending Mail, msg {}", e.getMessage(), e);
            }
        });
    }
}
