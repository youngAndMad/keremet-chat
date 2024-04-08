package danekerscode.keremetchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class KeremetChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeremetChatApplication.class, args);
	}



}
