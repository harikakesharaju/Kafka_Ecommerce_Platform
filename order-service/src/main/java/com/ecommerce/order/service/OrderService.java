
package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderItemRequest;
import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.context.ApplicationEventPublisher;

import com.ecommerce.order.event.OrderCreatedEvent;
import com.ecommerce.order.event.OrderItemEvent;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final ApplicationEventPublisher eventPublisher;

	public OrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {

		this.orderRepository = orderRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public Order createOrder(OrderRequest request) {

		Order order = new Order();
		order.setCustomerId(request.getCustomerId());
		order.setStatus("PENDING");
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (OrderItemRequest itemRequest : request.getItems()) {
			OrderItem item = new OrderItem();
			item.setProductId(itemRequest.getProductId());
			item.setProductName(itemRequest.getProductName());
			item.setQuantity(itemRequest.getQuantity());
			item.setUnitPrice(itemRequest.getUnitPrice());
			BigDecimal subtotal = item.getSubtotal();
			totalAmount = totalAmount.add(subtotal);
			order.addItem(item);
		}
		order.setTotalAmount(totalAmount);
		Order savedOrder = orderRepository.save(order);
		
		// Convert saved Order entity into an event.
		List<OrderItemEvent> eventItems = savedOrder.getItems().stream()
				.map(item -> new OrderItemEvent(item.getProductId(), item.getProductName(), item.getQuantity(),
						item.getUnitPrice(), item.getSubtotal()))
				.toList();
		OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), savedOrder.getCustomerId(),
				savedOrder.getTotalAmount(), savedOrder.getStatus(), savedOrder.getCreatedAt(), eventItems);

		// This event will be handled after the DB transaction commits.
		eventPublisher.publishEvent(event);

		return savedOrder;
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Order getOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
	}
}
