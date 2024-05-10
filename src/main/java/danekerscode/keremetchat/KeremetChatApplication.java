package danekerscode.keremetchat;

import danekerscode.keremetchat.config.properties.AppProperties;
import danekerscode.keremetchat.model.enums.websocket.WebsocketNotificationType;
import danekerscode.keremetchat.repository.ChatNotificationRepository;
import lombok.Data;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@EnableWebMvc
@EnableConfigurationProperties({AppProperties.class})
public class KeremetChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeremetChatApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(ChatNotificationRepository chatNotificationRepository) {
        return args -> {
            var test = new Test();
            test.setTest("some");

            var id = chatNotificationRepository.save(1L, LocalDateTime.now(), WebsocketNotificationType.MESSAGE, test);
        };
    }

    @Data
    public static class Test {
        String test;
    }


}
