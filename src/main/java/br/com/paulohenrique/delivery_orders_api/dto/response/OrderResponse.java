package br.com.paulohenrique.delivery_orders_api.dto.response;

import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Dados do pedido de entrega")
public record OrderResponse(
        @Schema(description = "Identificador único", example = "1")
        Long id,
        @Schema(description = "Nome do cliente", example = "Jane Doe")
        String customerName,
        @Schema(description = "Endereço de entrega", example = "123 Main St, Springfield, IL 62701")
        String address,
        @Schema(description = "Status do pedido")
        OrderStatus status,
        @Schema(description = "Data e hora de criação", example = "2026-03-11T12:00:00Z")
        Instant createdAt,
        @Schema(description = "Data e hora da última atualização", example = "2026-03-11T12:30:00Z")
        Instant updatedAt
) {
}
