package com.project.service.impl;

import com.project.dto.CouponDTO;
import com.project.dto.map.CouponMapper;
import com.project.entity.Coupon;
import com.project.entity.Customer;
import com.project.entity.Order;
import com.project.enums.CouponType;
import com.project.repository.CouponRepository;
import com.project.repository.CustomerRepository;
import com.project.repository.OrderRepository;
import com.project.repository.RestaurantRepository;
import com.project.service.CouponService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.Data ;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Data
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponMapper couponMapper;

    private final CouponRepository couponRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;


    @Override
    public Order removeCouponFromOrder(Long orderId, Long couponId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getCoupon() != null && order.getCoupon().getId().equals(couponId)) {
            order.setCoupon(null);
            order.calculateTotalAmount();
            orderRepository.save(order);
        }
        return order;
    }


    @Override
    public CouponDTO addCoupon(CouponDTO dto) {
        Coupon coupon = new Coupon();
        coupon.setCode(dto.getCode());
        coupon.setType(dto.getType());
        coupon.setDiscountValue(dto.getDiscountValue());

        // Set default dates if not provided
        coupon.setValidFrom(dto.getValidFrom() != null ? dto.getValidFrom() : LocalDateTime.now());
        coupon.setValidUntil(dto.getValidUntil() != null ? dto.getValidUntil() : LocalDateTime.now().plusDays(30));

        couponRepository.save(coupon);
        return couponMapper.toDTO(coupon);
    }


    @Override
    public CouponDTO updateCoupon(CouponDTO dto) {
        Coupon coupon = couponRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));

        // Update only if new value is provided, else keep old value
        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            coupon.setCode(dto.getCode());
        }

        if (dto.getDiscountValue() != 0) { // or better: check for null if Double wrapper is used
            coupon.setDiscountValue(dto.getDiscountValue());
        }

        // Add more fields if needed, e.g., validFrom, validUntil, type
        if (dto.getType() != null) {
            coupon.setType(dto.getType());
        }

        if (dto.getValidFrom() != null) {
            coupon.setValidFrom(dto.getValidFrom());
        }

        if (dto.getValidUntil() != null) {
            coupon.setValidUntil(dto.getValidUntil());
        }

        Coupon updatedCoupon = couponRepository.save(coupon);

        return couponMapper.toDTO(updatedCoupon);
    }

    @Override
    public void deleteCoupon(Long couponId) {
        if (!couponRepository.existsById(couponId)) {
            throw new EntityNotFoundException("Coupon not found");
        }
        couponRepository.deleteById(couponId);
    }

    @Override
    public List<CouponDTO> getAllCoupons() {
        List<Coupon> coupons = couponRepository.findAll();
        return couponMapper.toDtoList(coupons);
    }

    @Override
    public CouponDTO addCouponForCustomer(Long customerId, CouponDTO dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        // Check for duplicate coupon code
        if (couponRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Coupon code already exists");
        }

        // Validate dates
        LocalDateTime validFrom = dto.getValidFrom() != null ? dto.getValidFrom() : LocalDateTime.now();
        LocalDateTime validUntil = dto.getValidUntil() != null ? dto.getValidUntil() : validFrom.plusDays(30);
        if (validUntil.isBefore(validFrom)) {
            throw new IllegalArgumentException("Coupon validUntil cannot be before validFrom");
        }

        // Create coupon
        Coupon coupon = new Coupon();
        coupon.setCode(dto.getCode());
        coupon.setType(dto.getType());
        coupon.setDiscountValue(dto.getDiscountValue());
        coupon.setValidFrom(validFrom);
        coupon.setValidUntil(validUntil);

        couponRepository.save(coupon);

        // Associate with customer
        customer.getCoupons().add(coupon);
        customerRepository.save(customer);

        return couponMapper.toDTO(coupon);
    }


    @Override
    public Coupon getCouponById(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
    }

    @Override
    public List<Coupon> getCouponsByCode(String code) {
        return couponRepository.findByCodeContainingIgnoreCase(code);
    }

    @Override
    public List<Coupon> getCouponsByDiscountPercentage(double discountPercentage) {
        return couponRepository.findByDiscountValue(discountPercentage);
    }

    @Override
    public boolean isCouponValid(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
        return coupon.isActive();
    }

    @Override
    public List<Coupon> getCouponsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return customer.getCoupons();
    }

}
