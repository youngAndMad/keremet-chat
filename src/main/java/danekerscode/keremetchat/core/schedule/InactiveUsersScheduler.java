package danekerscode.keremetchat.core.schedule;

import danekerscode.keremetchat.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(name = "app.security.delete-inactive-users", havingValue = "true")
@Slf4j
@RequiredArgsConstructor
public class InactiveUsersScheduler {

    private final UserService userService;

    @PostConstruct
    void postConstruct() {
        log.info("InactiveUsersScheduler initialized");
    }

    @SchedulerLock(
            name = "InactiveUsersScheduler_InactiveUsersScheduler",
            lockAtLeastFor = "PT5M",
            lockAtMostFor = "PT14M"
    )
    @Scheduled(cron = "${app.security.delete-inactive-users-cron}")
    public void scheduleDeletionOfInactiveUsers() {
        var today = LocalDate.now();
        log.info("staring scheduleDeletionOfInactiveUsers for {}", today);

        var deletedInactiveUsers = userService.deleteInactiveUsers();
        log.info("finishing scheduleDeletionOfInactiveUsers for {}, deleted inactive users {}", today, deletedInactiveUsers);
    }

}
