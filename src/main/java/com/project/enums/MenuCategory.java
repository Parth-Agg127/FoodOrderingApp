package com.project.enums;

public enum MenuCategory {
    APPETIZER("Appetizer"),
    MAIN_COURSE("Main Course"),
    DESSERT("Dessert"),
    BEVERAGE("Beverage"),
    SOUP("Soup"),
    SALAD("Salad"),
    BREAD("Bread"),
    RICE("Rice"),
    NOODLES("Noodles"),
    PIZZA("Pizza"),
    BURGER("Burger"),
    SANDWICH("Sandwich"),
    SNACKS("Snacks"),
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner");

    private final String displayName;

    MenuCategory(String displayName) {
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
