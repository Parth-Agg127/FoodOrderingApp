package com.project.entity;

import com.project.enums.CouponType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "coupons")
@Data
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coupon code is required")
    @Size(min = 3, max = 50, message = "Coupon code must be between 3 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type", nullable = false)
    private CouponType type;

    @DecimalMin(value = "0.0", message = "Discount value cannot be negative")
    @Column(name = "discount_value", nullable = false, columnDefinition = "decimal(8,2)")
    private Double discountValue;

    @NotNull(message = "Valid from date is required")
    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @NotNull(message = "Valid until date is required")
    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;

    @OneToMany(mappedBy = "coupon")
    private List<Order> orders = new ArrayList<>();

    @ManyToMany(mappedBy = "coupons", fetch = FetchType.LAZY)
    private List<Customer> customers = new ArrayList<>();


    // Constructors
    public Coupon() {}

    public Coupon(String code, Double discountValue) {
        this.code = code;
        this.discountValue = discountValue;
    }
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(validFrom) && now.isBefore(validUntil);
    }





}
