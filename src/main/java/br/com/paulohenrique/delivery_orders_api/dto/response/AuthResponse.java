package br.com.paulohenrique.delivery_orders_api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de autenticação contendo com token de acesso")
public record AuthResponse(
        @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsIn...")
        String accessToken
) {
}
