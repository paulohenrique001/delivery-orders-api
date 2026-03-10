package br.com.paulohenrique.delivery_orders_api.controllers;

import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;
import br.com.paulohenrique.delivery_orders_api.dto.PageResponse;
import br.com.paulohenrique.delivery_orders_api.dto.mapper.OrderMapper;
import br.com.paulohenrique.delivery_orders_api.dto.request.CreateOrderRequest;
import br.com.paulohenrique.delivery_orders_api.dto.request.UpdateStatusOrderRequest;
import br.com.paulohenrique.delivery_orders_api.dto.response.OrderResponse;
import br.com.paulohenrique.delivery_orders_api.services.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

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

    @GetMapping
    public PageResponse<OrderResponse> findAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(required = false)
            OrderStatus status,
            @RequestParam(required = false)
            String customerName,
            @RequestParam(required = false)
            Instant startDate,
            @RequestParam(required = false)
            Instant endDate
    ) {
        Page<Order> page = orderService.findAll(
                pageable,
                status,
                customerName,
                startDate,
                endDate
        );

        return PageResponse.from(page.map(orderMapper::toResponse));
    }

    @GetMapping("/{id}")
    public OrderResponse findById(
            @PathVariable
            Long id
    ) {
        Order order = orderService.findByIdCached(id);
        return orderMapper.toResponse(order);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            UpdateStatusOrderRequest updateStatusOrderRequest

    ) {
        Order order = orderService.updateStatus(id, updateStatusOrderRequest.status());
        return orderMapper.toResponse(order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(
            @PathVariable
            Long id
    ) {
        orderService.cancel(id);
    }
}
