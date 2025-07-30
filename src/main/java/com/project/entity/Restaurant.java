package com.project.entity;
import com.project.enums.CuisineType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//
@Entity
@Table(name = "restaurants")
public class Restaurant {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank(message = "Restaurant name is required")
        @Size(min = 2, max = 200, message = "Restaurant name must be between 2 and 200 characters")
        @Column(nullable = false, length = 200)
        private String name;


        @NotBlank(message = "Address is required")
        @Size(max = 500, message = "Address cannot exceed 500 characters")
        @Column(nullable = false, length = 500)
        private String address;


        @Enumerated(EnumType.STRING)
        @Column(name = "cuisine_type", nullable = false)
        private CuisineType cuisineType;



        // One restaurant can have multiple menu items
        @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
        private List<MenuItem> menuItems = new ArrayList<>();

        // Constructors
        public Restaurant() {}

        public Restaurant(String name, String address, CuisineType cuisineType) {
            this.name = name;
            this.address = address;

            this.cuisineType = cuisineType;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }


        public CuisineType getCuisineType() { return cuisineType; }
        public void setCuisineType(CuisineType cuisineType) { this.cuisineType = cuisineType; }


        public List<MenuItem> getMenuItems() { return menuItems; }
        public void setMenuItems(List<MenuItem> menuItems) { this.menuItems = menuItems; }

    }

