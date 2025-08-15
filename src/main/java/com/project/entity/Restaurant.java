package com.project.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.enums.CuisineType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "restaurants")
@Data
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



        @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
        @JsonManagedReference
        private List<MenuItem> menuItems = new ArrayList<>();

        // Constructors
        public Restaurant() {}

        public Restaurant(String name, String address, CuisineType cuisineType) {
            this.name = name;
            this.address = address;

            this.cuisineType = cuisineType;
        }
}

