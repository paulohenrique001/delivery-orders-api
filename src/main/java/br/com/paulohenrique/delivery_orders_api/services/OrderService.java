package br.com.paulohenrique.delivery_orders_api.services;

import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order create(String customerName, String address) {
        Order order = Order.create(customerName, address);
        return orderRepository.save(order);
    }
}
