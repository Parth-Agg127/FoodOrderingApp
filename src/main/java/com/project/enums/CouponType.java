package com.project.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    PERCENTAGE("Percentage"),
    FIXED_AMOUNT("Fixed Amount"),
    FREE_DELIVERY("Free Delivery"),
    BUY_ONE_GET_ONE("Buy One Get One");

    private final String displayName;

    CouponType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
