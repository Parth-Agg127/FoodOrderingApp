package com.project.service.impl;

import com.project.entity.MenuItem;
import com.project.entity.Restaurant;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.MenuItemRepository;
import com.project.repository.RestaurantRepository;
import com.project.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public List<MenuItem> addMenuItems(Long restaurantId, List<MenuItem> menuItems) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with ID: " + restaurantId));

        for (MenuItem item : menuItems) {
            item.setRestaurant(restaurant);
        }

        return menuItemRepository.saveAll(menuItems);
    }


    @Override
    public MenuItem updateMenuItem(Long id, MenuItem updatedItem) {
        MenuItem existing = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        if (updatedItem.getName() != null) {
            existing.setName(updatedItem.getName());
        }
        if (updatedItem.getDescription() != null) {
            existing.setDescription(updatedItem.getDescription());
        }
        if (updatedItem.getPrice() != null) {
            existing.setPrice(updatedItem.getPrice());
        }


        // Don't update category or restaurant unless you intend to
        return menuItemRepository.save(existing);
    }


    @Override
    public void deleteMenuItem(Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with ID: " + menuItemId));

        menuItemRepository.delete(menuItem);
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Override
    public MenuItem getMenuItemById(Long menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with ID: " + menuItemId));
    }

    @Override
    public List<MenuItem> getAllMenuItemsByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }


}
