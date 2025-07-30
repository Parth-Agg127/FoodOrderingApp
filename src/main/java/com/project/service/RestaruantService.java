package com.project.service;

import com.project.entity.Restaurant;
import com.project.enums.CuisineType;

import java.util.List;
import java.util.Optional;

public interface RestaruantService {
    Restaurant createRestaurant(Restaurant restaurant);

    List<Restaurant> getAllRestaurants();

    Optional<Restaurant> getRestaurantById(Long id);

    Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant);

    void deleteRestaurant(Long id);

    List<Restaurant> getRestaurantsByCuisine(CuisineType cuisineType);
}
