package br.com.paulohenrique.delivery_orders_api.dto.request;

import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusOrderRequest(
        @NotNull(message = "Status é obrigatório")
        OrderStatus status
) {
}
