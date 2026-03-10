package br.com.paulohenrique.delivery_orders_api.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Bean(destroyMethod = "shutdown")
    public RedisClient redisClient(
            @Value("${spring.data.redis.host}")
            String host,
            @Value("${spring.data.redis.port}")
            int port,
            @Value("${spring.data.redis.password}")
            String password
    ) {
        return RedisClient.create(
                RedisURI.builder()
                        .withHost(host)
                        .withPort(port)
                        .withPassword(password.toCharArray())
                        .withTimeout(Duration.ofSeconds(2))
                        .build()
        );
    }
}
