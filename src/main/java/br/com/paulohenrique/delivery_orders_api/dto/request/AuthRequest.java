package br.com.paulohenrique.delivery_orders_api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
