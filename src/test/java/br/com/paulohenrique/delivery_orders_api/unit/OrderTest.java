package br.com.paulohenrique.delivery_orders_api.unit;

import br.com.paulohenrique.delivery_orders_api.domain.exception.business.InvalidStatusChangeException;
import br.com.paulohenrique.delivery_orders_api.domain.exception.business.OrderCannotBeCanceledException;
import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.domain.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    private Order order;

    @BeforeEach
    void setUp() {
        this.order = Order.create("John Doe", "123 Main St, Springfield, IL 62701");
    }

    @Test
    @DisplayName("O pedido deve ser criado com status CREATED")
    void create_setsStatusAsCreated() {
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
    }

    @Test
    @DisplayName("Um pedido pode ser cancelado se não estiver DELIVERED")
    void cancel_setsStatusAsCanceled() {
        order.cancel();

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    @DisplayName("Um pedido não pode ser cancelado se já estiver DELIVERED")
    void cancel_whenDelivered_throwsException() {
        order.updateStatus(OrderStatus.PROCESSING);
        order.updateStatus(OrderStatus.SHIPPED);
        order.updateStatus(OrderStatus.DELIVERED);

        assertThatThrownBy(() -> order.cancel())
                .isInstanceOf(OrderCannotBeCanceledException.class);
    }

    @Test
    @DisplayName("Status pode avançar")
    void updateStatus_withValidTransition_updatesStatus() {
        order.updateStatus(OrderStatus.PROCESSING);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PROCESSING);
    }

    @Test
    @DisplayName("Status não pode retroceder")
    void updateStatus_withInvalidTransition_throwsException() {
        order.updateStatus(OrderStatus.PROCESSING);
        order.updateStatus(OrderStatus.SHIPPED);

        assertThatThrownBy(() -> order.updateStatus(OrderStatus.PROCESSING))
                .isInstanceOf(InvalidStatusChangeException.class);
    }

    @Test
    @DisplayName("Pedido DELIVERED não pode ser alterado")
    void updateStatus_whenDelivered_throwsException() {
        order.updateStatus(OrderStatus.PROCESSING);
        order.updateStatus(OrderStatus.SHIPPED);
        order.updateStatus(OrderStatus.DELIVERED);

        assertThatThrownBy(() -> order.updateStatus(OrderStatus.CANCELED))
                .isInstanceOf(InvalidStatusChangeException.class);
    }
}
