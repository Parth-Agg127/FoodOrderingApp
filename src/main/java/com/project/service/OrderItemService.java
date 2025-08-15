package com.project.service;

import com.project.entity.Order;
import com.project.entity.OrderItem;

import java.util.List;

public interface OrderItemService {

    Order addItemToOrder(Long orderId, Long itemId, int quantity);

    List<OrderItem> getItemsByCustomerId(Long customerId);

    List<OrderItem> getItemsByOrderAndRestaurant(Long orderId, Long restaurantId);

    List<OrderItem> getItemsByOrderAndCustomer(Long orderId, Long customerId);

}
