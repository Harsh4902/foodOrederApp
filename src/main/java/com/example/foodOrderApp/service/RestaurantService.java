package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Restaurant;

import java.util.List;

public interface RestaurantService {

    List<Restaurant> getAllRestaurants();

    Restaurant addRestaurant(Restaurant restaurant);

    List<Restaurant> addRestaurants(List<Restaurant> restaurants);
}
