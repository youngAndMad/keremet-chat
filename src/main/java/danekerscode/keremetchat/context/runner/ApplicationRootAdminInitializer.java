package danekerscode.keremetchat.context.runner;

import danekerscode.keremetchat.config.properties.AppProperties;
import danekerscode.keremetchat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationRootAdminInitializer implements ApplicationRunner {

    private final UserService userService;
    private final AppProperties appProperties;

    @Override
    public void run(ApplicationArguments args) {
        var defaultAdmin = appProperties.getSecurity().getDefaultAdmin();
        userService.checkApplicationRootUser(defaultAdmin.getEmail(), defaultAdmin.getPassword());
    }

}
