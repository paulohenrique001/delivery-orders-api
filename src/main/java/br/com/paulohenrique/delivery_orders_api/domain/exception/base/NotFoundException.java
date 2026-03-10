package br.com.paulohenrique.delivery_orders_api.domain.exception.base;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
