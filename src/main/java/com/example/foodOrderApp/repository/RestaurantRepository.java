package com.example.foodOrderApp.repository;

import com.example.foodOrderApp.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

  Optional<Restaurant> findByEmail(String email);

}
