package br.com.paulohenrique.delivery_orders_api.dto;

import java.util.List;

public record ErrorResponse(
        String message,
        List<String> errors
) {
    public ErrorResponse(String message) {
        this(message, List.of());
    }
}
