package danekerscode.keremetchat.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class KeremetChatAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeremetChatAuthServerApplication.class, args);
	}

	@GetMapping("/me")
	Object me(){
		return SecurityContextHolder.getContext().getAuthentication();
			}


}
