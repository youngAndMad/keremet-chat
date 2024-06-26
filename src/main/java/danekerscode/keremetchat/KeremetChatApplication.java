package danekerscode.keremetchat;

import danekerscode.keremetchat.config.properties.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@EnableWebMvc
@EnableConfigurationProperties({AppProperties.class})
public class KeremetChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeremetChatApplication.class, args);
    }

}
