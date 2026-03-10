package br.com.paulohenrique.delivery_orders_api.domain.exception.business;

import br.com.paulohenrique.delivery_orders_api.domain.exception.base.UnprocessableContentException;

public class OrderCannotBeCanceledException extends UnprocessableContentException {
    public OrderCannotBeCanceledException() {
        super("Pedido entregue não pode ser cancelado");
    }
}
