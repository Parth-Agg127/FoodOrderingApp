package com.project.enums;

public enum CuisineType {
    INDIAN("Indian"),
    CHINESE("Chinese"),
    ITALIAN("Italian"),
    MEXICAN("Mexican"),
    AMERICAN("American"),
    THAI("Thai"),
    JAPANESE("Japanese"),
    MEDITERRANEAN("Mediterranean"),
    FAST_FOOD("Fast Food"),
    CONTINENTAL("Continental"),
    SOUTH_INDIAN("South Indian"),
    NORTH_INDIAN("North Indian"),
    PUNJABI("Punjabi"),
    GUJARATI("Gujarati"),
    BENGALI("Bengali");

    private final String displayName;

    CuisineType(String displayName) {
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
