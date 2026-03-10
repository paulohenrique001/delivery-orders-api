package br.com.paulohenrique.delivery_orders_api.services;

import br.com.paulohenrique.delivery_orders_api.domain.exception.base.NotFoundException;
import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;
import br.com.paulohenrique.delivery_orders_api.infrastructure.persistence.OrderSpecification;
import br.com.paulohenrique.delivery_orders_api.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public Order create(String customerName, String address) {
        Order order = Order.create(customerName, address);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Page<Order> findAll(
            Pageable pageable,
            OrderStatus status,
            String customerName,
            Instant startDate,
            Instant endDate
    ) {
        Specification<Order> specification = Specification
                .where(OrderSpecification.statusEqual(status))
                .and(OrderSpecification.customerNameStartsWith(customerName))
                .and(OrderSpecification.createdAtGreaterThanOrEqualTo(startDate))
                .and(OrderSpecification.createdAtLessThanOrEqualTo(endDate));

        return orderRepository.findAll(specification, pageable);
    }

    @Cacheable(value = "orders", key = "#id")
    @Transactional(readOnly = true)
    public Order findByIdCached(Long id) {
        return findById(id);
    }

    @CacheEvict(value = "orders", key = "#id")
    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order order = findById(id);
        order.updateStatus(status);

        return orderRepository.save(order);
    }

    @CacheEvict(value = "orders", key = "#id")
    @Transactional
    public void cancel(Long id) {
        Order order = findById(id);
        order.cancel();

        orderRepository.save(order);
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));
    }
}
