package com.project.repository;

import com.project.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);

    Optional<OrderItem> findByOrderIdAndMenuItemId(Long orderId, Long menuItemId);

    List<OrderItem> findByMenuItemRestaurantId(Long restaurantId);

    List<OrderItem> findByOrderCustomerId(Long customerId);

    List<OrderItem> findByOrderIdAndMenuItemRestaurantId(Long orderId, Long restaurantId);

    List<OrderItem> findByOrderIdAndOrderCustomerId(Long orderId, Long customerId);

}
