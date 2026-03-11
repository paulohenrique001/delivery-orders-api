package br.com.paulohenrique.delivery_orders_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados de autenticação do usuário")
public record AuthRequest(
        @Schema(description = "Nome de usuário", example = "admin")
        @NotBlank
        String username,
        @Schema(description = "Senha do usuário", example = "admin")
        @NotBlank
        String password
) {
}
