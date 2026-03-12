package br.com.paulohenrique.delivery_orders_api.infrastructure.messaging;

import br.com.paulohenrique.delivery_orders_api.dto.events.OrderShippedEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumer {
    @SqsListener("orders-queue")
    public void onOrderShippedEvent(OrderShippedEvent orderShippedEvent) {
        log.info("Cliente notificado sobre envio do pedido #{}", orderShippedEvent.orderId());
    }
}
