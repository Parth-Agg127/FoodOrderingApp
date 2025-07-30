package com.project.service;

import com.project.entity.MenuItem;

import java.util.List;

public interface MenuItemService {
    // Define methods for menu item operations, e.g., add, update, delete, get by ID, etc.
    void addMenuItem(Long restaurantId, String name, String description, double price);

    void updateMenuItem(Long menuItemId, String name, String description, double price);

    void deleteMenuItem(Long menuItemId);

    MenuItem getMenuItemById(Long menuItemId);

    List<MenuItem> getAllMenuItemsByRestaurant(Long restaurantId);
}
