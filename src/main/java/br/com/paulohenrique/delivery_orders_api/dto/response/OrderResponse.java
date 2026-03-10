package br.com.paulohenrique.delivery_orders_api.dto.response;

import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;

import java.time.Instant;

public record OrderResponse(
        Long id,
        String customerName,
        String address,
        OrderStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
