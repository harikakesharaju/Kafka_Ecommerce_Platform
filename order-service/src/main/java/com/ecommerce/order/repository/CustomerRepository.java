
package com.ecommerce.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.order.entity.Customer;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);
}
