package com.example.foodOrderApp.service;

import com.example.foodOrderApp.entity.Restaurant;
import com.example.foodOrderApp.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    @Override
    public Page<Restaurant> getPaginatedRestaurants(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<Restaurant> list;
        if(restaurants.size() < startItem)
            list = Collections.emptyList();
        else{
            int toIndex = Math.min(startItem + pageSize, restaurants.size());
            list = restaurants.subList(startItem, toIndex);
        }
        Page<Restaurant> restaurantPage = new PageImpl<>(list, PageRequest.of(currentPage,pageSize), restaurants.size());
        return restaurantPage;
    }
}
