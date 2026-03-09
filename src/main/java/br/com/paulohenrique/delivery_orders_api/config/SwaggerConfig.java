package br.com.paulohenrique.delivery_orders_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Delivery Orders API")
                .description("API REST utilizando Java 17 e Spring Boot para gerenciamento de pedidos de entrega.")
                .version("v1"));
    }
}
