package br.com.paulohenrique.delivery_orders_api.domain.exception.base;

public class UnprocessableContentException extends RuntimeException {
    public UnprocessableContentException(String message) {
        super(message);
    }
}
