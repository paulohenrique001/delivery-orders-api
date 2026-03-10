package br.com.paulohenrique.delivery_orders_api.domain.model;

import br.com.paulohenrique.delivery_orders_api.domain.exception.business.InvalidStatusChangeException;
import br.com.paulohenrique.delivery_orders_api.domain.exception.business.OrderCannotBeCanceledException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

@Table(name = "tb_orders")
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String customerName;
    @Column(nullable = false, length = 500)
    private String address;
    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
    @Version
    private Integer version;

    public static Order create(String customerName, String address) {
        Order order = new Order();
        order.customerName = customerName;
        order.address = address;
        order.status = OrderStatus.CREATED;

        return order;
    }

    public void updateStatus(OrderStatus next) {
        if (!status.canChangeTo(next)) {
            throw new InvalidStatusChangeException(status, next);
        }

        this.status = next;
    }

    public void cancel() {
        if (status.isDelivered()) {
            throw new OrderCannotBeCanceledException();
        }

        this.status = OrderStatus.CANCELED;
    }
}
