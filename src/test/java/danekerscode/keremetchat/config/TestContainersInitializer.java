package danekerscode.keremetchat.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public class TestContainersInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.1"));
    static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    static {
        Startables.deepStart(postgres, redis).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of(
                "spring.datasource.url=" + postgres.getJdbcUrl(),
                "spring.datasource.username=" + postgres.getUsername(),
                "spring.datasource.password=" + postgres.getPassword(),
                "spring.redis.host=" + redis.getHost(),
                "spring.redis.port=" + redis.getMappedPort(6379).toString()
        ).applyTo(ctx.getEnvironment());
    }

}
