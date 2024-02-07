package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Restaurant;
import com.example.foodOrderApp.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService{
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @Override
    public Restaurant addRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> addRestaurants(List<Restaurant> restaurants) {
        return restaurantRepository.saveAll(restaurants);
    }
}
