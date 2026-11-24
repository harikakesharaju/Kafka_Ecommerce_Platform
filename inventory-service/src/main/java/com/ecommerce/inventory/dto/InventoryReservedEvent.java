
package com.ecommerce.inventory.dto;

import java.time.LocalDateTime;

public class InventoryReservedEvent {

    private Long orderId;
    private String status;
    private LocalDateTime reservedAt;

    public InventoryReservedEvent() {
    }

    public InventoryReservedEvent(Long orderId, String status,
                                  LocalDateTime reservedAt) {
        this.orderId = orderId;
        this.status = status;
        this.reservedAt = reservedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }
}
