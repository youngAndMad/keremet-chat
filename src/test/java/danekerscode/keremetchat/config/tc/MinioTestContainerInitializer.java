package danekerscode.keremetchat.config.tc;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

public class MinioTestContainerInitializer
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String MINIO_IMAGE_NAME = "minio";

    static GenericContainer<?> minio =
            new GenericContainer<>(DockerImageName.parse(MINIO_IMAGE_NAME)).withExposedPorts(9000);

    public static final String MINIO_ADMIN = "minioadmin";

    static {
        minio.addEnv("MINIO_ROOT_USER", MINIO_ADMIN);
        minio.addEnv("MINIO_ROOT_PASSWORD", MINIO_ADMIN);

        Startables.deepStart(minio).join();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of(
                "app.minio.url=" + ("http://localhost:9000"),
                "app.minio.access-key=" + MINIO_ADMIN,
                "app.minio.secret-key=" + MINIO_ADMIN
        ).applyTo(ctx.getEnvironment());
    }

}
