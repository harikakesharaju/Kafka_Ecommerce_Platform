
package com.ecommerce.inventory.consumer;

import java.time.LocalDateTime;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.ecommerce.inventory.dto.OrderCreatedEvent;
import com.ecommerce.inventory.dto.InventoryReservedEvent;

import com.ecommerce.inventory.producer.InventoryEventProducer;
import com.ecommerce.inventory.service.InventoryService;

@Component
public class OrderCreatedConsumer {

	private final InventoryService inventoryService;
	private final ObjectMapper objectMapper;
	private final InventoryEventProducer eventProducer;

	public OrderCreatedConsumer(InventoryService inventoryService, ObjectMapper objectMapper,
			InventoryEventProducer eventProducer) {
		this.inventoryService = inventoryService;
		this.objectMapper = objectMapper;
		this.eventProducer = eventProducer;
	}

	@KafkaListener(topics = "order-created", groupId = "inventory-service-group")
	public void consume(String message) throws Exception {
		System.out.println("Received Kafka event: " + message);
		OrderCreatedEvent event = objectMapper.readValue(message, OrderCreatedEvent.class);
		// Step 1: Reserve stock
		inventoryService.reserveStock(event);
		// Step 2: Create success event
		InventoryReservedEvent reservedEvent = new InventoryReservedEvent(event.getOrderId(), "RESERVED",
				LocalDateTime.now());
		// Step 3: Publish success event
		eventProducer.publishInventoryReserved(reservedEvent);
	}
}
