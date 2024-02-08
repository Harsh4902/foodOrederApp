package com.example.foodOrderApp.repository;

import com.example.foodOrderApp.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer,Long> {

    List<Offer> findAllByRestaurantName(String restaurantName);

}
