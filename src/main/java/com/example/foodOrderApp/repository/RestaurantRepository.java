package com.example.foodOrderApp.repository;

import com.example.foodOrderApp.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
}
