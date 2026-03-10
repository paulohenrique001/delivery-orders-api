package br.com.paulohenrique.delivery_orders_api.domain.exception.business;

import br.com.paulohenrique.delivery_orders_api.domain.exception.base.UnprocessableContentException;
import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;

public class InvalidStatusChangeException extends UnprocessableContentException {
    public InvalidStatusChangeException(OrderStatus current, OrderStatus next) {
        super("Não é permitido mudar do status %s para %s".formatted(current, next));
    }
}
