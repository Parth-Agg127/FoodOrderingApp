package com.project.service;

import com.project.entity.MenuItem;

import java.util.List;

public interface MenuItemService {
    // Define methods for menu item operations, e.g., add, update, delete, get by ID, etc.
    List<MenuItem> addMenuItems(Long restaurantId, List<MenuItem> menuItems);

    MenuItem updateMenuItem(Long menuItemId, MenuItem menuItem);

    void deleteMenuItem(Long menuItemId);

    MenuItem getMenuItemById(Long menuItemId);

    List<MenuItem> getAllMenuItemsByRestaurant(Long restaurantId);

    List<MenuItem> getAllMenuItems();
}
