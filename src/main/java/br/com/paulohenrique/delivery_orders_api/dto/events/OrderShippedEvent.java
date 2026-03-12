package br.com.paulohenrique.delivery_orders_api.dto.events;

import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;

import java.time.Instant;

public record OrderShippedEvent(
        Long orderId,
        OrderStatus status,
        Instant timestamp
) {
}
