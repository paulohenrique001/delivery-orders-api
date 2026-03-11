package br.com.paulohenrique.delivery_orders_api.unit;

import br.com.paulohenrique.delivery_orders_api.domain.exception.base.NotFoundException;
import br.com.paulohenrique.delivery_orders_api.domain.model.Order;
import br.com.paulohenrique.delivery_orders_api.repositories.OrderRepository;
import br.com.paulohenrique.delivery_orders_api.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    private Order order;
    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        this.order = Order.create("John Doe", "123 Main St, Springfield, IL 62701");
    }

    @Test
    @DisplayName("Deve retornar pedido quando encontrado")
    void findByIdCached_whenFound_returnsOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.findByIdCached(1L);

        assertThat(result).isEqualTo(order);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não encontrado")
    void findByIdCached_whenNotFound_throwsNotFoundException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findByIdCached(999L))
                .isInstanceOf(NotFoundException.class);
    }
}
