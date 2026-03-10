package br.com.paulohenrique.delivery_orders_api.domain.model;

public enum OrderStatus {
    CREATED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELED;

    public boolean canChangeTo(OrderStatus nextOrderStatus) {
        return switch (this) {
            case CREATED -> nextOrderStatus == PROCESSING || nextOrderStatus == CANCELED;
            case PROCESSING -> nextOrderStatus == SHIPPED || nextOrderStatus == CANCELED;
            case SHIPPED -> nextOrderStatus == DELIVERED;
            default -> false;
        };
    }
}
