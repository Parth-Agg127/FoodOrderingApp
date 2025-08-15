package com.project.repository;

import com.project.entity.Coupon;
import com.project.entity.Customer;
import com.project.entity.Order;
import com.project.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByOrderDateBetween(LocalDate start, LocalDate end);
    List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);

    List<Order> findByRestaurantIdAndStatus(Long restaurantId, OrderStatus status);

    int countByCustomerId(Long customerId);

    boolean existsByCustomerAndCoupon (Customer customer, Coupon coupon);
}
