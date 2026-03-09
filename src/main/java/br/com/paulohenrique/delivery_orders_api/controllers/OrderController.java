package br.com.paulohenrique.delivery_orders_api.controllers;

import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.dto.CreateOrderRequest;
import br.com.paulohenrique.delivery_orders_api.dto.OrderResponse;
import br.com.paulohenrique.delivery_orders_api.dto.mapper.OrderMapper;
import br.com.paulohenrique.delivery_orders_api.services.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderMapper orderMapper;
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(
            @Valid
            @RequestBody
            CreateOrderRequest createOrderRequest
    ) {
        Order order = orderService.create(
                createOrderRequest.customerName(),
                createOrderRequest.address()
        );
        return orderMapper.toResponse(order);
    }
}
