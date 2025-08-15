package com.project.controller;

import com.project.dto.*;
import com.project.dto.map.OrderMapper;
import com.project.entity.*;
import com.project.enums.OrderStatus;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.CustomerRepository;
import com.project.service.*;
import com.project.config.JwtUtil;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    // Hardcoded admin credentials
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemService orderitemService;

    @Autowired
    private CustomerService customerService;
    // -----------------------------------------------
    // 🔐 Admin login endpoint
    // -----------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        if (ADMIN_USERNAME.equals(loginDTO.getEmail()) && ADMIN_PASSWORD.equals(loginDTO.getPassword())) {
            String token = jwtUtil.generateToken(loginDTO.getEmail());
            return ResponseEntity.ok(new LoginResponseDTO(token, "Login successful"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid admin credentials");
        }
    }

    // -----------------------------------------------
    // 🏪 Restaurant Endpoints
    // -----------------------------------------------

    @PostMapping("/restaurants")
    public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant dto) {
        return new ResponseEntity<>(restaurantService.createRestaurant(dto), HttpStatus.CREATED);
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant dto) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, dto));
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping("/restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok("Restaurant deleted successfully");
    }

    // -----------------------------------------------
    // 🍽️ Menu Item Endpoints
    // -----------------------------------------------

    @PostMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItem>> addMenuItems(@PathVariable Long restaurantId,
                                                       @RequestBody List<MenuItem> menuItems) {
        List<MenuItem> savedItems = menuItemService.addMenuItems(restaurantId, menuItems);
        return new ResponseEntity<>(savedItems, HttpStatus.CREATED);
    }


    @PutMapping("/menu-items/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem dto) {
        return ResponseEntity.ok(menuItemService.updateMenuItem(id, dto));
    }

    @GetMapping("/menu-items")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<String> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        return ResponseEntity.ok("Menu item deleted successfully");
    }

    @GetMapping("/restaurants/{restaurantId}/menu-items")
    public ResponseEntity<List<MenuItem>> getAllMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItem> menuItems = menuItemService.getAllMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    // -----------------------------------------------
    // 🎟️ Coupon Endpoints
    // -----------------------------------------------

    @PostMapping("/coupons")
    public ResponseEntity<CouponDTO> addCoupon(@RequestBody CouponDTO dto) {
        return new ResponseEntity<>(couponService.addCoupon(dto), HttpStatus.CREATED);
    }

    @PostMapping("/coupons/{customerId}")
    public ResponseEntity<CouponDTO> addCouponForCustomer(
            @PathVariable Long customerId,
            @RequestBody @Valid CouponDTO couponDTO) {

        CouponDTO createdCoupon = couponService.addCouponForCustomer(customerId, couponDTO);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }

    @PutMapping("/coupons/{id}")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Long id, @RequestBody CouponDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(couponService.updateCoupon(dto));
    }

    @GetMapping("/coupons")
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon deleted successfully");
    }

    //--------------------------------------------------
    // 📦 Order Endpoints
    //--------------------------------------------------
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> ordersDTO = orderService.getAllOrdersDTO();
        return ResponseEntity.ok(ordersDTO);
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok("Order deleted successfully");
    }

    @GetMapping("/restaurants/{restaurantId}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        List<OrderResponseDTO> ordersDTO = orderService.getOrdersByRestaurant(restaurantId);
        return ResponseEntity.ok(ordersDTO);
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
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully");
    }

    @PutMapping("/{orderId}/complete")
    public ResponseEntity<String> completeOrder(@PathVariable Long orderId) {
        orderService.completeOrder(orderId);
        return ResponseEntity.ok("Order marked as delivered successfully");
    }

    @GetMapping("/restaurant/{restaurantId}/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByRestaurantAndStatus(
            @PathVariable Long restaurantId,
            @PathVariable OrderStatus status) {
        List<Order> orders = orderService.getOrdersByRestaurantAndStatus(restaurantId, status);
        return ResponseEntity.ok(orders);
    }

    // -----------------------------------------------
    // 📦 Order Item Endpoints
    // -----------------------------------------------
    @GetMapping("/orders/{orderId}/restaurant/{restaurantId}/items")
    public ResponseEntity<List<OrderItem>> getItemsByOrderAndRestaurant(
            @PathVariable Long orderId,
            @PathVariable Long restaurantId) {

        List<OrderItem> items = orderitemService.getItemsByOrderAndRestaurant(orderId, restaurantId);
        return ResponseEntity.ok(items);
    }



    // 1. Get Customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // 2. Update Customer
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        customerService.updateCustomer(id, updatedCustomer);
        return ResponseEntity.noContent().build();
    }

    // 3. Delete Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    // 4. Get Customers by Address
    @GetMapping("/address/{address}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByAddress(@PathVariable String address) {
        List<CustomerDTO> customers = customerService.getCustomersByAddress(address);
        return ResponseEntity.ok(customers);
    }

    // 5. Get All Customers
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // 6. Get Customers by Name
    @GetMapping("/name/{name}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByName(@PathVariable String name) {
        List<CustomerDTO> customers = customerService.getCustomersByName(name);
        return ResponseEntity.ok(customers);
    }

    // 7. Get Customers by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByEmail(@PathVariable String email) {
        List<CustomerDTO> customers = customerService.getCustomersByEmail(email);
        return ResponseEntity.ok(customers);
    }

    // 8. Get Customers by Phone Number
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByPhoneNumber(@PathVariable String phoneNumber) {
        List<CustomerDTO> customers = customerService.getCustomersByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(customers);
    }



}
