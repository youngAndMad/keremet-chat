package danekerscode.keremetchat.context.runner;

import danekerscode.keremetchat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationRootAdminInitializer implements ApplicationRunner {

    private final UserService userService;

    @Value("${app.security.default-admin.email}")
    private String applicationRootAdminEmail;
    @Value("${app.security.default-admin.password}")
    private String applicationRootAdminPassword;

    @Override
    public void run(ApplicationArguments args){
        userService.checkApplicationRootUser(applicationRootAdminEmail,applicationRootAdminPassword);
    }

}
