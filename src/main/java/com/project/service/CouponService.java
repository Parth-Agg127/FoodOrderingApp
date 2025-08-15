package com.project.service;

import com.project.dto.CouponDTO;
import com.project.entity.Coupon;
import com.project.entity.Order;

import java.util.List;

public interface CouponService {

    Order removeCouponFromOrder(Long orderId, Long couponId);

    CouponDTO addCoupon(CouponDTO dto);

    CouponDTO addCouponForCustomer(Long customerId, CouponDTO dto);

    CouponDTO updateCoupon(CouponDTO dto);

    void deleteCoupon(Long couponId);

    List<CouponDTO> getAllCoupons();

    Coupon getCouponById(Long couponId);

    List<Coupon> getCouponsByCode(String code);

    List<Coupon> getCouponsByDiscountPercentage(double discountPercentage);

    boolean isCouponValid(Long couponId);

    List<Coupon> getCouponsByCustomer(Long customerId);



}
