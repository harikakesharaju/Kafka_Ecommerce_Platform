
package com.ecommerce.order.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderCreatedEvent(
        Long orderId,
        Long customerId,
        BigDecimal totalAmount,
        String status,
        LocalDateTime createdAt,
        List<OrderItemEvent> items
) {
}
