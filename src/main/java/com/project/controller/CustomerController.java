package com.project.controller;

import com.project.dto.OrderResponseDTO;
import com.project.dto.PlaceOrderRequestDTO;
import com.project.dto.map.OrderMapper;
import com.project.entity.*;
import com.project.enums.CuisineType;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.CustomerRepository;
import com.project.repository.RestaurantRepository;
import com.project.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuItemService menuItemService;

    // Public endpoint (no authentication required)
    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Public endpoint: No authentication needed!");
    }

    // Protected endpoint (authentication required via JWT)
    @GetMapping("/me")
    public ResponseEntity<Customer> getLoggedInCustomer(Authentication authentication) {
        String email = authentication.getName(); // Extracted from JWT
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return ResponseEntity.ok(customer);
    }

    //------------------------------------------
    // 🏪 Restaurant Endpoints
    //------------------------------------------


    // Get all restaurants
    @GetMapping("/restaurants")
    public ResponseEntity<?> getAllRestaurants() {
        return ResponseEntity.ok(restaurantRepo.findAll());
    }


    // Place a new order
    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @Valid @RequestBody PlaceOrderRequestDTO request,
            Authentication authentication) {

        String email = authentication.getName();
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order newOrder = orderService.createOrder(
                customer.getId(),
                request.getRestaurantId(),
                request.getCouponId(),
                request.getItems(),
                request.getDelivery_Address()
        );

        OrderResponseDTO response = orderMapper.toOrderResponseDTO(newOrder);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));

        List<Order> orders = orderService.getOrdersByCustomer(customer.getId());

        List<OrderResponseDTO> response = orderMapper.toOrderResponseDTOs(orders);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId, Authentication authentication) {
        String email = authentication.getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = orderService.getOrderById(orderId);

        if (!order.getCustomer().getId().equals(customer.getId())) {
            return ResponseEntity.status(403).body("You are not authorized to view this order");
        }

        OrderResponseDTO response = orderMapper.toOrderResponseDTO(order);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/orders/{orderId}/coupon/{couponId}")
    public ResponseEntity<OrderResponseDTO> removeCouponFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long couponId) {

        Order updatedOrder = couponService.removeCouponFromOrder(orderId, couponId);
        OrderResponseDTO updatedOrderDTO = orderMapper.toOrderResponseDTO(updatedOrder);

        return ResponseEntity.ok(updatedOrderDTO);
    }
    @PostMapping("/orders/{orderId}/items")
    public ResponseEntity<OrderResponseDTO> addItemToOrder(
            @PathVariable Long orderId,
            @RequestParam Long itemId,
            @RequestParam int quantity) {

        Order updatedOrder = orderItemService.addItemToOrder(orderId, itemId, quantity);

        OrderResponseDTO dto = orderMapper.toOrderResponseDTO(updatedOrder);

        return ResponseEntity.ok(dto);
    }
    @GetMapping("/restaurants/by-cuisine")
    public ResponseEntity<List<Restaurant>> getRestaurantsByCuisine(@RequestParam CuisineType cuisineType) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByCuisine(cuisineType);
        return ResponseEntity.ok(restaurants);
    }
    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        return ResponseEntity.ok(restaurant);
    }
    @GetMapping("/orders/by-date")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
        List<OrderResponseDTO> orderDTOs = orders.stream()
                .map(orderMapper::toOrderResponseDTO)
                .toList();

        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/{orderId}/total-price")
    public ResponseEntity<Double> getTotalPrice(@PathVariable Long orderId) {
        double totalPrice = orderService.calculateTotalPrice(orderId);
        return ResponseEntity.ok(totalPrice);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrder(@PathVariable Long orderId) {
        List<OrderItem> orderItems = orderService.getOrderItemsByOrder(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/{orderId}/status")
    public ResponseEntity<String> getOrderStatus(@PathVariable Long orderId) {
        String status = orderService.getOrderStatus(orderId).name();
        return ResponseEntity.ok(status);
    }
    @GetMapping("/customer/{customerId}/items")
    public ResponseEntity<List<OrderItem>> getItemsByCustomerId(@PathVariable Long customerId) {
        List<OrderItem> items = orderItemService.getItemsByCustomerId(customerId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/orders/{orderId}/customer/{customerId}/items")
    public ResponseEntity<List<OrderItem>> getItemsByOrderAndRestaurant(
            @PathVariable Long orderId,
            @PathVariable Long customerId) {

        List<OrderItem> items = orderItemService.getItemsByOrderAndCustomer(orderId, customerId);
        return ResponseEntity.ok(items);
    }
    @GetMapping("/menu-items/{menuItemId}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long menuItemId) {
        MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);
        return ResponseEntity.ok(menuItem);
    }

    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItem>> getAllMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItem> menuItems = menuItemService.getAllMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    // 1️⃣ Get coupon by ID
    @GetMapping("/coupon/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    // 2️⃣ Search coupons by code
    @GetMapping("/coupon/by-code")
    public ResponseEntity<List<Coupon>> getCouponsByCode(@RequestParam String code) {
        List<Coupon> coupons = couponService.getCouponsByCode(code);
        return ResponseEntity.ok(coupons);
    }

    // 3️⃣ Search coupons by discount percentage
    @GetMapping("/coupon/by-discount")
    public ResponseEntity<List<Coupon>> getCouponsByDiscount(@RequestParam double discount) {
        List<Coupon> coupons = couponService.getCouponsByDiscountPercentage(discount);
        return ResponseEntity.ok(coupons);
    }

    // 4️⃣ Check if coupon is valid
    @GetMapping("/coupon/{id}/valid")
    public ResponseEntity<Boolean> isCouponValid(@PathVariable Long id) {
        boolean isValid = couponService.isCouponValid(id);
        return ResponseEntity.ok(isValid);
    }

    // 5️⃣ Get all coupons for a customer
    @GetMapping("/coupon/customer/{customerId}")
    public ResponseEntity<List<Coupon>> getCouponsByCustomer(@PathVariable Long customerId) {
        List<Coupon> coupons = couponService.getCouponsByCustomer(customerId);
        return ResponseEntity.ok(coupons);
    }



}
