package danekerscode.keremetchat.common.annotation;

import danekerscode.keremetchat.config.tc.RedisTestContainersInitializer;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(initializers = RedisTestContainersInitializer.class)
public @interface EnableRedisContainer {
}
