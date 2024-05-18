package danekerscode.keremetchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisConfig {

    private static final int CACHE_TTL = 10;
    private static final GenericJackson2JsonRedisSerializer GENERIC_JACKSON_2_JSON_REDIS_SERIALIZER
            = new GenericJackson2JsonRedisSerializer();

    @Bean
    RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(CACHE_TTL))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(GENERIC_JACKSON_2_JSON_REDIS_SERIALIZER)
                );
    }

    @Bean
    <K, V> RedisTemplate<K, V> redisTemplate(
            RedisConnectionFactory redisConnectionFactory
    ) {
        var redisTemplate = new RedisTemplate<K, V>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(GENERIC_JACKSON_2_JSON_REDIS_SERIALIZER);
        redisTemplate.setValueSerializer(GENERIC_JACKSON_2_JSON_REDIS_SERIALIZER);

        return redisTemplate;
    }

}