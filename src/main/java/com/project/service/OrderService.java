package com.project.service;

import com.project.dto.OrderItemDTO;
import com.project.dto.OrderResponseDTO;
import com.project.entity.Order;
import com.project.entity.OrderItem;
import com.project.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    //give me all the required methods for the class OrderService
    Order createOrder(Long customerId, Long restaurantId, Long couponId, List<OrderItemDTO> orderItemsDTO,String deliveryAddress);

    List<OrderResponseDTO> getAllOrdersDTO();

    Order applyCoupon(Long orderId, String couponCode, String customerEmail);

    Order getOrderById(Long orderId);

    void deleteOrder(Long orderId);

    List<Order> getOrdersByCustomer(Long customerId);

    List<OrderResponseDTO> getOrdersByRestaurant(Long restaurantId);

    List<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate);

    double calculateTotalPrice(Long orderId);

    List<OrderItem> getOrderItemsByOrder(Long orderId);

    OrderStatus getOrderStatus(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus status);

    void cancelOrder(Long orderId);

    void completeOrder(Long orderId);

    List<Order> getOrdersByRestaurantAndStatus(Long restaurantId, OrderStatus status);

   }
