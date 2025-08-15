package com.project.service.impl;
import com.project.entity.MenuItem;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.entity.Restaurant;
import com.project.enums.CuisineType;
import com.project.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        for (MenuItem item : restaurant.getMenuItems()) {
            item.setRestaurant(restaurant); // link both sides
        }
        return restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        if (updatedRestaurant.getName() != null) {
            existing.setName(updatedRestaurant.getName());
        }
        if (updatedRestaurant.getAddress() != null) {
            existing.setAddress(updatedRestaurant.getAddress());
        }
        if (updatedRestaurant.getCuisineType() != null) {
            existing.setCuisineType(updatedRestaurant.getCuisineType());
        }
        // Add checks for other fields if you have them

        return restaurantRepository.save(existing);
    }


    @Override
    public void deleteRestaurant(Long id) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        restaurantRepository.delete(existing);
    }

    @Override
    public List<Restaurant> getRestaurantsByCuisine(CuisineType cuisineType) {
        return restaurantRepository.findByCuisineType(cuisineType);
    }
}
