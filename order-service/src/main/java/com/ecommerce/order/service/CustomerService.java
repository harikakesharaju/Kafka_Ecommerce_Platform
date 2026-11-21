
package com.ecommerce.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.order.dto.CustomerRequest;
import com.ecommerce.order.entity.Customer;
import com.ecommerce.order.repository.CustomerRepository;

@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Customer createCustomer(CustomerRequest request) {
		if (customerRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Customer already exists with this email");
		}
		Customer customer = new Customer();
		customer.setName(request.getName());
		customer.setEmail(request.getEmail());
		customer.setPhone(request.getPhone());
		return customerRepository.save(customer);
	}

	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	public Customer getCustomerById(Long id) {
		return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found: " + id));
	}
}
