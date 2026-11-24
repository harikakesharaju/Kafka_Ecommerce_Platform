
package com.ecommerce.inventory.producer;

import java.util.concurrent.ExecutionException;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ecommerce.inventory.dto.InventoryReservedEvent;

@Component
public class InventoryEventProducer {

	private static final String TOPIC = "inventory-reserved";

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public InventoryEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	public void publishInventoryReserved(InventoryReservedEvent event) throws Exception {

		String message = objectMapper.writeValueAsString(event);

		kafkaTemplate.send(TOPIC, event.getOrderId().toString(), message).get();

		System.out.println("Published InventoryReserved event: " + message);
	}
}
