package danekerscode.keremetchat.config;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class SchedulerConfiguration {

    /**
     * Method which will create bean to control  locking of scheduled tasks
     * @param dataSource param to point connection settings to database, will be injected by Spring
     * @return LockProvider to control locking of scheduled tasks
     * @author youngAndMad
     */
    @Bean
    LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

}