package br.com.paulohenrique.delivery_orders_api.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Bean(destroyMethod = "shutdown")
    public RedisClient redisClient(RedisConnectionFactory connectionFactory) {
        LettuceConnectionFactory factory = (LettuceConnectionFactory) connectionFactory;
        RedisStandaloneConfiguration config = factory.getStandaloneConfiguration();

        RedisURI.Builder builder = RedisURI.builder()
                .withHost(config.getHostName())
                .withPort(config.getPort())
                .withTimeout(Duration.ofSeconds(2));

        config.getPassword().toOptional()
                .ifPresent(builder::withPassword);

        return RedisClient.create(builder.build());
    }
}
