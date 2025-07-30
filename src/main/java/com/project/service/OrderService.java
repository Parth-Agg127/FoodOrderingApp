package com.project.service;

import com.project.entity.Order;
import com.project.entity.OrderItem;
import com.project.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    //give me all the required methods for the class OrderService
    void createOrder(Long customerId, Long couponId, List<Long> orderItemIds);

    List<Order> getAllOrders();

    Order getOrderById(Long orderId);

    Order updateOrder(Long orderId, Order updatedOrder);

    void deleteOrder(Long orderId);

    List<Order> getOrdersByCustomer(Long customerId);

    List<Order> getOrdersByRestaurant(Long restaurantId);


    List<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate);

    double calculateTotalPrice(Long orderId);

    void applyCouponToOrder(Long orderId, Long couponId);

    void addOrderItemToOrder(Long orderId, Long orderItemId);

    void removeOrderItemFromOrder(Long orderId, Long orderItemId);

    List<OrderItem> getOrderItemsByOrder(Long orderId);

    OrderStatus getOrderStatus(Long orderId);

    void updateOrderStatus(Long orderId, OrderStatus status);

    void cancelOrder(Long orderId);

    void completeOrder(Long orderId);

    List<Order> getOrdersByCustomerAndStatus(Long customerId, OrderStatus status);

    List<Order> getOrdersByRestaurantAndStatus(Long restaurantId, OrderStatus status);

    List<Order> getOrdersByCustomerAndDateRange(Long customerId, LocalDate startDate, LocalDate endDate);

    List<Order> getOrdersByRestaurantAndDateRange(Long restaurantId, LocalDate startDate,LocalDate endDate);
}
