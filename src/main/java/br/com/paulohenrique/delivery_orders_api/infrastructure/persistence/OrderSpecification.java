package br.com.paulohenrique.delivery_orders_api.infrastructure.persistence;

import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class OrderSpecification {
    public static Specification<Order> statusEqual(OrderStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Order> customerNameStartsWith(String customerName) {
        return (root, query, cb) ->
                customerName == null ? null : cb.like(
                        cb.lower(root.get("customerName")), customerName.toLowerCase() + "%"
                );
    }

    public static Specification<Order> createdAtGreaterThanOrEqualTo(Instant startDate) {
        return (root, query, cb) ->
                startDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
    }

    public static Specification<Order> createdAtLessThanOrEqualTo(Instant endDate) {
        return (root, query, cb) ->
                endDate == null ? null : cb.lessThanOrEqualTo(root.get("createdAt"), endDate);
    }
}
