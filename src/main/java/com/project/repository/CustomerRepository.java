package com.project.repository;

import com.project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer , Long> {
    List<Customer> findByAddress(String address);
    List<Customer> findByPhoneNumber(String phoneNumber);
    List<Customer> findByNameContainingIgnoreCase(String name);
    Optional<Customer> findByEmail(String email);
    List<Customer> findAllByEmail(String email);  // for admin search (if needed)
}
