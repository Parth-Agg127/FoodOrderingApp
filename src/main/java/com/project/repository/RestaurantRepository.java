package com.project.repository;

import com.project.entity.Restaurant;
import com.project.enums.CuisineType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCuisineType(CuisineType cuisineType);
}
