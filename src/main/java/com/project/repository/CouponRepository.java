package com.project.repository;

import com.project.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon , Long> {
    List<Coupon> findByCodeContainingIgnoreCase(String code);
    List<Coupon> findByDiscountValue(double discountValue);
    boolean existsByCode(String code);
    Optional<Coupon> findByCode(String code);
}
