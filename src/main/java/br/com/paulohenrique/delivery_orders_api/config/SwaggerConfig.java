package br.com.paulohenrique.delivery_orders_api.config;

import br.com.paulohenrique.delivery_orders_api.dto.response.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "bearerAuth";

        return new OpenAPI().info(buildApiInfo())
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName, buildSecurityScheme()));
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            if (operation.getRequestBody() != null) {
                operation.getResponses().addApiResponse("400", buildBadRequestResponse());
            }

            return operation;
        };
    }

    private Info buildApiInfo() {
        return new Info().title("Delivery Orders API")
                .description("API REST utilizando Java 17 e Spring Boot para gerenciamento de pedidos de entrega.");
    }

    private SecurityScheme buildSecurityScheme() {
        return new SecurityScheme().name("BEARER TOKEN")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Token de acesso JWT");
    }

    private ApiResponse buildBadRequestResponse() {
        List<String> errosExamples = List.of(
                "Campo 'x' inválido",
                "Campo 'y' é obrigatório"
        );
        ErrorResponse errorResponse = new ErrorResponse("Erro de Validação", errosExamples);

        Content content = new Content().addMediaType(
                "application/json",
                new MediaType().schema(new Schema<>().example(errorResponse))
        );

        return new ApiResponse().description("Erro de validação")
                .content(content);
    }
}
