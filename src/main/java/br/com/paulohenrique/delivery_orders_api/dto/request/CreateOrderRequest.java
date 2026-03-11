package br.com.paulohenrique.delivery_orders_api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação do pedido de entrega")
public record CreateOrderRequest(
        @Size(min = 3, max = 100, message = "Nome do cliente deve ter entre 3 e 100 caracteres")
        @Schema(description = "Nome do cliente", example = "John Doe")
        @NotBlank(message = "Nome do cliente é obrigatório")
        String customerName,
        @Size(min = 5, max = 500, message = "Endereço deve ter entre 5 e 500 caracteres")
        @Schema(description = "Endereço de entrega", example = "123 Main St, Springfield, IL 62701")
        @NotBlank(message = "Endereço é obrigatório")
        String address
) {
}
