package com.project.entity;

import com.project.enums.CouponType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coupon code is required")
    @Size(min = 3, max = 50, message = "Coupon code must be between 3 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String code;


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


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Double getDiscountValue() { return discountValue; }
    public void setDiscountValue(Double discountValue) { this.discountValue = discountValue; }

    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }

    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }


}
