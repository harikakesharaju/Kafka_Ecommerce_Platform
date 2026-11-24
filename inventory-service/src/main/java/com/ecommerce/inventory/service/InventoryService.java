
package com.ecommerce.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.inventory.dto.OrderCreatedEvent;
import com.ecommerce.inventory.dto.OrderItemEvent;
import com.ecommerce.inventory.entity.Product;
import com.ecommerce.inventory.repository.ProductRepository;

@Service
public class InventoryService {

    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void reserveStock(OrderCreatedEvent event) {

        System.out.println("Processing order: " + event.getOrderId());

        for (OrderItemEvent item : event.getItems()) {

            Product product = productRepository
                    .findByIdForUpdate(item.getProductId())
                    .orElseThrow(() -> new RuntimeException(
                            "Product not found: " + item.getProductId()));

            int available = product.getAvailableQuantity();
            int requested = item.getQuantity();

            if (requested <= 0) {
                throw new RuntimeException("Invalid quantity for product: "
                        + item.getProductId());
            }

            if (available < requested) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName());
            }

            product.setAvailableQuantity(available - requested);

            productRepository.save(product);

            System.out.println(
                    "Reserved " + requested + " units of " + product.getName());
        }

        System.out.println(
                "Inventory reservation completed for order: " + event.getOrderId());
    }
}
