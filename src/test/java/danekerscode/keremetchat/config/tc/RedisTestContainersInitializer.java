package danekerscode.keremetchat.config.tc;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public class RedisTestContainersInitializer implements
        ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String REDIS_IMAGE_NAME = "postgres:15.1";

    static GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE_NAME)).withExposedPorts(6379);

    static {
        Startables.deepStart(redis).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of(
                "spring.redis.host=" + redis.getHost(),
                "spring.redis.port=" + redis.getMappedPort(6379).toString()
        ).applyTo(ctx.getEnvironment());
    }

}
