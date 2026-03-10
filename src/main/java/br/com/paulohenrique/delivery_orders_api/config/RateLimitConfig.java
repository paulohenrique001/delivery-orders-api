package br.com.paulohenrique.delivery_orders_api.config;

import br.com.paulohenrique.delivery_orders_api.filters.RateLimitFilter;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@AllArgsConstructor
public class RateLimitConfig {
    private final RateLimitProperties properties;

    @Bean
    public StatefulRedisConnection<byte[], byte[]> redisConnection(RedisClient redisClient) {
        return redisClient.connect(ByteArrayCodec.INSTANCE);
    }

    @Bean
    public LettuceBasedProxyManager<byte[]> proxyManager(StatefulRedisConnection<byte[], byte[]> redisConnection) {
        return LettuceBasedProxyManager.builderFor(redisConnection)
                .build();
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(properties.getCapacity())
                        .refillGreedy(properties.getCapacity(), Duration.ofMinutes(properties.getRefillMinutes()))
                        .build())
                .build();
    }

    @Bean
    public RateLimitFilter rateLimitFilter(
            LettuceBasedProxyManager<byte[]> proxyManager,
            BucketConfiguration bucketConfiguration
    ) {
        return new RateLimitFilter(proxyManager, bucketConfiguration, properties);
    }

    @Bean
    public FilterRegistrationBean<?> rateLimitFilterRegistration(RateLimitFilter rateLimitFilter) {
        FilterRegistrationBean<?> registration = new FilterRegistrationBean<>(rateLimitFilter);
        registration.setEnabled(false);

        return registration;
    }
}
