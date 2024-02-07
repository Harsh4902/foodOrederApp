package com.example.foodOrderApp.conroller;

import com.example.foodOrderApp.entity.Restaurant;
import com.example.foodOrderApp.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/add-list")
    public @ResponseBody ResponseEntity addRestaurants(@RequestBody List<Restaurant> restaurants){
        return new ResponseEntity(restaurantService.addRestaurants(restaurants),HttpStatus.CREATED);
    }

}
