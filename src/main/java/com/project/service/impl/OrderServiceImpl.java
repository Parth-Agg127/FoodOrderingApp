package com.project.service.impl;

import com.project.dto.OrderItemDTO;
import com.project.dto.OrderResponseDTO;
import com.project.dto.map.OrderMapper;
import com.project.entity.*;
import com.project.enums.CouponType;
import com.project.enums.OrderStatus;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.*;
import com.project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private MenuItemRepository menuItemRepository;

    private String generateOrderNumber() {
        // Example format: ORD-20250813-123456
        return "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS"));
    }

    @Override
    public Order createOrder(Long customerId, Long restaurantId, Long couponId, List<OrderItemDTO> orderItemsDTO, String deliveryAddress) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        if (!isAddressServiceable(restaurant.getAddress(), deliveryAddress)) {
            throw new IllegalArgumentException("Sorry, this restaurant does not deliver to the provided address.");
        }

        if (orderItemsDTO == null || orderItemsDTO.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0.0;

        for (OrderItemDTO dto : orderItemsDTO) {
            MenuItem menuItem = menuItemRepository.findById(dto.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

            if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
                throw new IllegalArgumentException("Menu item " + menuItem.getId() + " does not belong to the specified restaurant");
            }

            double itemTotal = menuItem.getPrice() * dto.getQuantity();

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setPrice(itemTotal);
            orderItems.add(orderItem);

            subtotal += itemTotal;
        }

        // Create order
        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderNumber(generateOrderNumber());
        order.setDelivery_address(deliveryAddress);
        order.setSubtotal(subtotal);
        order.setDiscount(0.0);
        order.setGst(0.0);
        order.setTotalAmount(subtotal);

        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }

        // Save to get orderId
        order = orderRepository.save(order);

        // Apply coupon if given
        if (couponId != null) {
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
            order = applyCoupon(order.getId(), coupon.getCode(), customer.getEmail());
        } else {
            // If no coupon, apply GST directly
            double gstRate = 0.18; // 18%
            double gst = subtotal * gstRate;
            order.setGst(gst);
            order.setTotalAmount(subtotal + gst);
            orderRepository.save(order);
        }

        return order;
    }

    @Override
    public Order applyCoupon(Long orderId, String couponCode, String customerEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!coupon.getCustomers().contains(customer)) {
            throw new RuntimeException("This coupon is not valid for this customer");
        }

        // 🔹 Special rule for FIRST50 — only once per customer
        if ("FIRST50".equalsIgnoreCase(couponCode)) {
            boolean alreadyUsed = orderRepository.existsByCustomerAndCoupon(customer, coupon);
            if (alreadyUsed) {
                throw new RuntimeException("FIRST50 coupon can only be used once per customer");
            }
        }

        // Calculate discount
        double discount = 0.0;
        if (coupon.getType() == CouponType.PERCENTAGE) {
            discount = order.getSubtotal() * (coupon.getDiscountValue() / 100.0);
        } else if (coupon.getType() == CouponType.FIXED_AMOUNT) {
            discount = coupon.getDiscountValue();
        }

        order.setDiscount(discount);

        // GST after discount
        double priceAfterDiscount = order.getSubtotal() - discount;
        double gstRate = 0.18; // 18%
        double gst = priceAfterDiscount * gstRate;

        order.setGst(gst);
        order.setTotalAmount(priceAfterDiscount + gst);

        order.setCoupon(coupon);

        // Maintain relations
        if (!customer.getCoupons().contains(coupon)) {
            customer.getCoupons().add(coupon);
        }
        if (!coupon.getCustomers().contains(customer)) {
            coupon.getCustomers().add(customer);
        }

        customerRepository.save(customer);
        couponRepository.save(coupon);
        orderRepository.save(order);

        return order;
    }



    private boolean isAddressServiceable(String restaurantAddress, String deliveryAddress) {
        if (restaurantAddress == null || deliveryAddress == null) {
            return false;
        }

        // Simplest approach: compare lowercased city/area substring
        return deliveryAddress.toLowerCase().contains(getCityFromAddress(restaurantAddress).toLowerCase());
    }

    private String getCityFromAddress(String address) {
        // Naive way: last part after last comma (can be replaced with a better parser)
        String[] parts = address.split(",");
        return parts[parts.length - 1].trim();
    }



    private double calculateItemsTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();
    }

    @Override
    public List<OrderResponseDTO> getAllOrdersDTO() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toOrderResponseDTOs(orders);
    }


    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }


    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        orderRepository.delete(order);
    }

    @Override
    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<OrderResponseDTO> getOrdersByRestaurant(Long restaurantId) {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        return orderMapper.toOrderResponseDTOs(orders);
    }


    @Override
    public List<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }

    @Override
    public double calculateTotalPrice(Long orderId) {
        Order order = getOrderById(orderId);
        double basePrice = calculateItemsTotal(order.getOrderItems());
        double discount = order.getCoupon() != null ? order.getCoupon().getDiscountValue() : 0;
        return basePrice - discount;
    }

    @Override
    public List<OrderItem> getOrderItemsByOrder(Long orderId) {
        return getOrderById(orderId).getOrderItems();
    }

    @Override
    public OrderStatus getOrderStatus(Long orderId) {
        return getOrderById(orderId).getStatus();
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    @Override
    public void completeOrder(Long orderId) {
        Order order = getOrderById(orderId);

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot complete a cancelled order");
        }

        updateOrderStatus(orderId, OrderStatus.DELIVERED);
    }

    @Override
    public List<Order> getOrdersByRestaurantAndStatus(Long restaurantId, OrderStatus status) {
        return orderRepository.findByRestaurantIdAndStatus(restaurantId, status);
    }


}
