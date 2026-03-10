package br.com.paulohenrique.delivery_orders_api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {
    private int capacity;
    private int refillMinutes;
}
