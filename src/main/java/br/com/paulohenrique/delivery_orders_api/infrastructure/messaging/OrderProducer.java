package br.com.paulohenrique.delivery_orders_api.infrastructure.messaging;

import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.dto.mapper.OrderMapper;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderProducer {
    private final OrderMapper orderMapper;
    private final SqsTemplate sqsTemplate;
    private static final String QUEUE_NAME = "orders-queue";

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderShipped(Order order) {
        sqsTemplate.send(QUEUE_NAME, orderMapper.toOrderShippedEvent(order));
    }
}
