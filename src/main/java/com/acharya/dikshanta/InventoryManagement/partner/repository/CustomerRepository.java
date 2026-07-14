package com.acharya.dikshanta.InventoryManagement.partner.repository;

import com.acharya.dikshanta.InventoryManagement.partner.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
