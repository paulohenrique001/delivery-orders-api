package br.com.paulohenrique.delivery_orders_api.dto.mapper;

import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.dto.events.OrderShippedEvent;
import br.com.paulohenrique.delivery_orders_api.dto.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);

    @Mapping(source = "id", target = "orderId")
    @Mapping(target = "timestamp", expression = "java(Instant.now())")
    OrderShippedEvent toOrderShippedEvent(Order order);
}
