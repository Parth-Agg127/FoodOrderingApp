package com.project.enums;

public enum CouponType {
    PERCENTAGE("Percentage"),
    FIXED_AMOUNT("Fixed Amount"),
    FREE_DELIVERY("Free Delivery"),
    BUY_ONE_GET_ONE("Buy One Get One");

    private final String displayName;

    CouponType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
