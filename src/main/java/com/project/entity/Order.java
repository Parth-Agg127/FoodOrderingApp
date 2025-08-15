package com.project.entity;

import com.project.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Order number is required")
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @NotNull(message = "Order date is required")
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @DecimalMin(value = "0.0", message = "Total amount cannot be negative")
    @Column(name = "total_amount", nullable = false, columnDefinition = "decimal(10,2)")
    private Double totalAmount = 0.0;

    // Many orders belong to one customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Many orders belong to one restaurant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    // One order can have multiple order items
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "subtotal", nullable = false)
    private double subtotal;

    @Column(name = "gst", nullable = false)
    private double gst;  // store GST amount

    @Column(name = "discount", nullable = false)
    private double discount; // store discount amount applied

    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address cannot exceed 500 characters")
    @Column(nullable = false, length = 500)
    private String delivery_address;

    // Constructors
    public Order() {
        this.orderDate = LocalDateTime.now();
    }

    public Order(Customer customer, Restaurant restaurant) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.orderDate = LocalDateTime.now();
    }

    public Order(Customer customer, Restaurant restaurant, Coupon coupon) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.coupon = coupon;
        this.orderDate = LocalDateTime.now();
        this.orderNumber = generateOrderNumber();
    }


    // Billing calculation logic
    public void calculateTotalAmount() {
        double subtotal = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        double discount = (coupon != null && coupon.isActive()) ? coupon.getDiscountValue() : 0.0;
        double tax = subtotal * 0.05;
        this.totalAmount = Math.max(0.0, subtotal + tax - discount);

    }

    private String generateOrderNumber() {

        return "ORD" + System.currentTimeMillis() + (int) (Math.random() * 1000);
    }

    }
