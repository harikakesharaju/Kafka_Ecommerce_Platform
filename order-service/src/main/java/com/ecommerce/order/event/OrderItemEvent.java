
package com.ecommerce.order.event;

import java.math.BigDecimal;

public record OrderItemEvent(
        Long productId,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
