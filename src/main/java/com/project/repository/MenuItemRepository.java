package com.project.repository;

import com.project.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    // This interface will automatically provide CRUD operations for MenuItem entities
    // Additional custom query methods can be defined here if needed
}
