package br.com.paulohenrique.delivery_orders_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(
        @NotBlank(message = "Nome do cliente é obrigatório")
        @Size(min = 3, max = 100, message = "Nome do cliente deve ter entre 3 e 100 caracteres")
        String customerName,
        @NotBlank(message = "Endereço é obrigatório")
        @Size(min = 5, max = 500, message = "Endereço deve ter entre 5 e 500 caracteres")
        String address
) {
}
