package br.com.paulohenrique.delivery_orders_api.dto.request;

import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para atualização de status do pedido")
public record UpdateStatusOrderRequest(
        @Schema(description = "Novo status", example = "PROCESSING")
        @NotNull(message = "Status é obrigatório")
        OrderStatus status
) {
}
