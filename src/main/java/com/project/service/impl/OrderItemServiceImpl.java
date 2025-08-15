package com.project.service.impl;

import com.project.entity.Order;
import com.project.entity.OrderItem;
import com.project.entity.MenuItem;
import com.project.entity.Customer;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.OrderItemRepository;
import com.project.repository.OrderRepository;
import com.project.repository.MenuItemRepository;
import com.project.repository.CustomerRepository;
import com.project.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Order addItemToOrder(Long orderId, Long itemId, int quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        MenuItem menuItem = menuItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + itemId));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(menuItem.getPrice() * quantity);

        orderItemRepository.save(orderItem);

        // Update total amount of the order
        double newTotal = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();

        order.setTotalAmount(newTotal);
        orderRepository.save(order);

        return order;  // return updated order to controller
    }

    @Override
    public List<OrderItem> getItemsByCustomerId(Long customerId) {
        return orderItemRepository.findByOrderCustomerId(customerId);
    }

    @Override
    public List<OrderItem> getItemsByOrderAndRestaurant(Long orderId, Long restaurantId) {
        return orderItemRepository.findByOrderIdAndMenuItemRestaurantId(orderId, restaurantId);
    }

    @Override
    public List<OrderItem> getItemsByOrderAndCustomer(Long orderId, Long customerId) {
        return orderItemRepository.findByOrderIdAndOrderCustomerId(orderId, customerId);
    }
}
