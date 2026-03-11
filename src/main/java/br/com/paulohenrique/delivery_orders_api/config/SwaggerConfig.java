package br.com.paulohenrique.delivery_orders_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                        .title("Delivery Orders API")
                        .description("API REST utilizando Java 17 e Spring Boot para gerenciamento de pedidos de entrega.")
                ).addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes(
                                "bearerAuth",
                                new SecurityScheme().name("BEARER TOKEN")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .description("Token de acesso JWT")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
